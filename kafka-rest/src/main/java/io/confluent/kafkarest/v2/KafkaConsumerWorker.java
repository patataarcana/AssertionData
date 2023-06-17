/**
 * Copyright 2017 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package io.confluent.kafkarest.v2;

import io.confluent.kafkarest.ConsumerWorkerReadCallback;
import io.confluent.kafkarest.KafkaRestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Worker thread for consumers that multiplexes multiple consumer operations onto a single thread.
 */
public class KafkaConsumerWorker extends Thread {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumerWorker.class);

  KafkaRestConfig config;

  AtomicBoolean isRunning = new AtomicBoolean(true);
  CountDownLatch shutdownLatch = new CountDownLatch(1);

  Queue<KafkaConsumerReadTask> tasks = new LinkedList<KafkaConsumerReadTask>();
  Queue<KafkaConsumerReadTask> waitingTasks =
      new PriorityQueue<KafkaConsumerReadTask>(1, new ReadTaskExpirationComparator());

  public KafkaConsumerWorker(KafkaRestConfig config) {
    this.config = config;
  }

  public synchronized <KafkaK, KafkaV, ClientK, ClientV>
  Future readRecords(KafkaConsumerState state, long timeout, long maxBytes,
      ConsumerWorkerReadCallback<ClientK, ClientV> callback) {
    log.trace("KafkaConsumer worker " + this.toString() + " for " + state.getId());
    KafkaConsumerReadTask<KafkaK, KafkaV, ClientK, ClientV> task
        = new KafkaConsumerReadTask<KafkaK, KafkaV, ClientK, ClientV>(state, null, timeout,
        maxBytes, callback);
    if (!task.isDone()) {
      tasks.add(task);
      this.notifyAll();
    }
    return task;
  }

  @Override
  public void run() {
    while (isRunning.get()) {
      KafkaConsumerReadTask task = null;
      synchronized (this) {
        if (tasks.isEmpty()) {
          try {
            long now = config.getTime().milliseconds();
            long nextExpiration = nextWaitingExpiration();
            if (nextExpiration > now) {
              long timeout = (nextExpiration == Long.MAX_VALUE ?
                  0 : nextExpiration - now);
              assert (timeout >= 0);
              config.getTime().waitOn(this, timeout);
            }
          } catch (InterruptedException e) {
            // Indication of shutdown
          }
        }

        long now = config.getTime().milliseconds();
        while (nextWaitingExpiration() <= now) {
          tasks.add(waitingTasks.remove());
        }

        task = tasks.poll();
        if (task != null) {
          boolean backoff = task.doPartialRead();
          if (!task.isDone()) {
            if (backoff) {
              waitingTasks.add(task);
            } else {
              tasks.add(task);
            }
          }
        }
      }
    }
    shutdownLatch.countDown();
  }

  private long nextWaitingExpiration() {
    if (waitingTasks.isEmpty()) {
      return Long.MAX_VALUE;
    } else {
      return waitingTasks.peek().waitExpiration;
    }
  }

  public void shutdown() {
    try {
      isRunning.set(false);
      this.interrupt();
      shutdownLatch.await();
    } catch (InterruptedException e) {
      log.error("Interrupted while "
          + "consumer worker thread.");
      throw new Error("Interrupted when shutting down consumer worker thread.");
    }
  }

  private static class ReadTaskExpirationComparator implements Comparator<KafkaConsumerReadTask> {

    @Override
    public int compare(KafkaConsumerReadTask t1, KafkaConsumerReadTask t2) {
      if (t1.waitExpiration == t2.waitExpiration) {
        return 0;
      } else if (t1.waitExpiration < t2.waitExpiration) {
        return -1;
      } else {
        return 1;
      }
    }
  }
}
