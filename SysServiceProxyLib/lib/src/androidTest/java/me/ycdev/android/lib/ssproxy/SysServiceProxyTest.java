package me.ycdev.android.lib.ssproxy;

import android.app.AlarmManager;
import android.content.Context;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import java.util.concurrent.TimeUnit;

import eu.chainfire.libsuperuser.Debug;
import eu.chainfire.libsuperuser.Shell;
import me.ycdev.android.lib.common.compat.PowerManagerCompat;
import me.ycdev.android.lib.common.internalapi.android.os.PowerManagerIA;
import me.ycdev.android.lib.ssproxy.proxy.ISysServiceProxy;
import me.ycdev.android.lib.ssproxy.proxy.SysServiceProxyNative;

public class SysServiceProxyTest extends AndroidTestCase {
    public SysServiceProxyTest() {
        Debug.setDebug(true);
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);

        long endTime = SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(10);
        while (null == context.getApplicationContext()) {
            if (SystemClock.elapsedRealtime() >= endTime) {
                fail("fail to get application context initialized");
            }
            SystemClock.sleep(100);
        }
    }

    public void test_startDaemon() {
        assertTrue("wasn't granted root permission", Shell.SU.available());

        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());

        ssp.startDaemon();
        assertTrue("failed to start daemon", ssp.isDaemonAlive());

        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());
    }

    public void test_stopDaemon() {
        assertTrue("wasn't granted root permission", Shell.SU.available());

        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.startDaemon();
        assertTrue("failed to start daemon", ssp.isDaemonAlive());

        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());
    }

    public void test_getSspVersion() {
        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());

        // ssp will be added
        int lowSspVersion = ISysServiceProxy.SSP_VERSION;
        ssp.doStartDaemon(android.os.Process.myUid(), lowSspVersion);
        assertTrue("failed to start daemon", ssp.isDaemonAlive());
        assertEquals("ssp version not compatible", lowSspVersion, ssp.getSspVersion());

        // ssp will be updated
        int highSspVersion = ISysServiceProxy.SSP_VERSION + 1;
        ssp.doStartDaemon(android.os.Process.myUid(), highSspVersion);
        assertTrue("failed to start daemon", ssp.isDaemonAlive());
        assertEquals("ssp version not compatible", highSspVersion, ssp.getSspVersion());

        // no change
        ssp.doStartDaemon(android.os.Process.myUid(), lowSspVersion);
        assertTrue("failed to start daemon", ssp.isDaemonAlive());
        assertEquals("ssp version not compatible", highSspVersion, ssp.getSspVersion());
    }

    public void test_getService() {
        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.startDaemon();
        assertTrue("failed to start daemon", ssp.isDaemonAlive());

        IBinder powerBinder = ssp.getService(Context.POWER_SERVICE);
        doTestScreenOnOff(powerBinder);

        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());
    }

    private void doTestScreenOnOff(IBinder powerBinder) {
        assertNotNull("failed to get power binder", powerBinder);

        Object powerService = PowerManagerIA.asInterface(powerBinder);
        assertNotNull("failed to get power service", powerService);

        PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        if (!PowerManagerCompat.isScreenOn(pm)) {
            //noinspection deprecation
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "test");
            try {
                wakeLock.acquire();
                SystemClock.sleep(1000);
                assertTrue("failed to turn of the screen", PowerManagerCompat.isScreenOn(pm));
            } finally {
                wakeLock.release();
            }
        }

        PowerManagerIA.goToSleep(powerService, SystemClock.uptimeMillis());
        SystemClock.sleep(1000);
        assertFalse("failed to turn off the screen", PowerManagerCompat.isScreenOn(pm));

        AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, 0, null);
        //noinspection deprecation
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "test");
        try {
            wakeLock.acquire();
            SystemClock.sleep(1000);
            assertTrue("failed to turn on the screen", PowerManagerCompat.isScreenOn(pm));
        } finally {
            wakeLock.release();
        }
    }

    public void test_checkService() {
        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.startDaemon();
        assertTrue("failed to start daemon", ssp.isDaemonAlive());

        IBinder powerBinder = ssp.checkService(Context.POWER_SERVICE);
        doTestScreenOnOff(powerBinder);

        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());
    }

    public void testSspPermissionCheck() {
        SysServiceProxy ssp = SysServiceProxy.getInstance(getContext());
        ssp.stopDaemon();
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());

        int uid = android.os.Process.myUid();
        ssp.doStartDaemon(uid + 1, ISysServiceProxy.SSP_VERSION);

        try {
            ssp.getSspVersion();
            fail("permission checking failed");
        } catch (SecurityException ignored) {
            // expected
        }

        try {
            ssp.getService(Context.POWER_SERVICE);
            fail("permission checking failed");
        } catch (SecurityException ignored) {
            // expected
        }

        try {
            ssp.checkService(Context.POWER_SERVICE);
            fail("permission checking failed");
        } catch (SecurityException ignored) {
            // expected
        }

        try {
            IBinder sspBinder = new SysServiceProxyNative(android.os.Process.myUid(),
                    ISysServiceProxy.SSP_VERSION);
            ssp.addService("ssp_test", sspBinder);
            fail("permission checking failed");
        } catch (SecurityException ignored) {
            // expected
        }

        try {
            ssp.listServices();
            fail("permission checking failed");
        } catch (SecurityException ignored) {
            // expected
        }

        ssp.doStopDaemon(uid + 1);
        assertFalse("failed to stop daemon", ssp.isDaemonAlive());
    }
}
