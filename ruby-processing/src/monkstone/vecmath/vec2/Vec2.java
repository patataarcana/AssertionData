package monkstone.vecmath.vec2;
/* 
* Copyright (C) 2015-16 Martin Prout
* 
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
* 
* http://creativecommons.org/licenses/LGPL/2.1/
* 
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyBoolean;
import org.jruby.RubyClass;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Arity;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import monkstone.vecmath.JRender;

/**
*
* @author Martin Prout
*/
@JRubyClass(name = "Vec2D")
public class Vec2 extends RubyObject {
  
  static final double EPSILON = 9.999999747378752e-05; // matches processing.org EPSILON
  private static final long serialVersionUID = -7013225882277559392L;
  private double jx = 0;
  private double jy = 0;
  
  /**
  *
  * @param runtime
  */
  public static void createVec2(final Ruby runtime) {
    RubyClass vec2Cls = runtime.defineClass("Vec2D", runtime.getObject(), new ObjectAllocator() {
        @Override
        public IRubyObject allocate(Ruby runtime, RubyClass rubyClass) {
          return new Vec2(runtime, rubyClass);
        }
    });
    vec2Cls.defineAnnotatedMethods(Vec2.class);
    
  }
  
  /**
  *
  * @param context
  * @param klazz
  * @param args optional (no args jx = 0, jy = 0)
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "new", meta = true, rest = true)
  public static final IRubyObject rbNew(ThreadContext context, IRubyObject klazz, IRubyObject[] args) {
    Vec2 vec2 = (Vec2) ((RubyClass) klazz).allocate();
    vec2.init(context, args);
    return vec2;
  }
  
  /**
  *
  * @param runtime
  * @param klass
  */
  public Vec2(Ruby runtime, RubyClass klass) {
    super(runtime, klass);
  }
  
  void init(ThreadContext context, IRubyObject[] args) {
    if (Arity.checkArgumentCount(context.getRuntime(), args, Arity.OPTIONAL.getValue(), 2) == 2) {
      jx = (Double) args[0].toJava(Double.class);
      jy = (Double) args[1].toJava(Double.class);
    }
  }
  
  /**
  *
  * @param context
  * @return jx float
  */
  @JRubyMethod(name = "x")
  
  public IRubyObject getX(ThreadContext context) {
    return context.getRuntime().newFloat(jx);
  }
  
  /**
  *
  * @param context
  * @return jy float
  */
  @JRubyMethod(name = "y")
  
  public IRubyObject getY(ThreadContext context) {
    return context.getRuntime().newFloat(jy);
  }
  
  /**
  *
  * @param context
  * @param other
  * @return jx float
  */
  @JRubyMethod(name = "x=")
  
  public IRubyObject setX(ThreadContext context, IRubyObject other) {
    jx = (Double) other.toJava(Double.class);
    return other;
  }
  
  /**
  *
  * @param context
  * @param other
  * @return jy float
  */
  @JRubyMethod(name = "y=")
  
  public IRubyObject setY(ThreadContext context, IRubyObject other) {
    jy = (Double) other.toJava(Double.class);
    return other;
  }
  
  /**
  *
  * @param context
  * @param other
  * @return hypotenuse float
  */
  @JRubyMethod(name = "dist", required = 1)
  
  public IRubyObject dist(ThreadContext context, IRubyObject other) {
    Vec2 b = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      b = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    double result = Math.hypot((jx - b.jx), (jy - b.jy));
    return runtime.newFloat(result);
  }
  
  /**
  *
  * @param context
  * @param other
  * @return cross product as a new Vec3D
  */
  @JRubyMethod(name = "cross", required = 1)
  
  public IRubyObject cross(ThreadContext context, IRubyObject other) {
    Vec2 b = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      b = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    return runtime.newFloat(jx * b.jy - jy * b.jx);
  }
  
  /**
  *
  * @param context
  * @param other
  * @return do product as a float
  */
  @JRubyMethod(name = "dot", required = 1)
  
  public IRubyObject dot(ThreadContext context, IRubyObject other) {
    Vec2 b = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      b = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    return runtime.newFloat(jx * b.jx + jy * b.jy);
  }
  
  /**
  *
  * @param context
  * @param other
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "+", required = 1)
  
  public IRubyObject op_plus(ThreadContext context, IRubyObject other) {
    Vec2 b = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      b = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    return Vec2.rbNew(context, other.getMetaClass(), new IRubyObject[]{
      runtime.newFloat(jx + b.jx), 
      runtime.newFloat(jy + b.jy)});
  }
  
  /**
  *
  * @param context
  * @param other
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "-", required = 1)
  
  public IRubyObject op_minus(ThreadContext context, IRubyObject other) {
    Vec2 b = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      b = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    return Vec2.rbNew(context, other.getMetaClass(), new IRubyObject[]{
      runtime.newFloat(jx - b.jx),
      runtime.newFloat(jy - b.jy)});
  }
  
  /**
  *
  * @param context
  * @param other
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "*")
  
  public IRubyObject op_mul(ThreadContext context, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    double scalar = (Double) other.toJava(Double.class);
    return Vec2.rbNew(context, this.getMetaClass(),
      new IRubyObject[]{runtime.newFloat(jx * scalar),
      runtime.newFloat(jy * scalar)});
  }
  
  /**
  *
  * @param context
  * @param other
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "/", required = 1)
  
  public IRubyObject op_div(ThreadContext context, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    double scalar = (Double) other.toJava(Double.class);
    if (Math.abs(scalar) < Vec2.EPSILON) {
      return this;
    }
    return Vec2.rbNew(context, this.getMetaClass(), new IRubyObject[]{
        runtime.newFloat(jx / scalar),
        runtime.newFloat(jy / scalar)});
  }
  
  /**
  *
  * @param context
  * @return angle radians as a float
  */
  @JRubyMethod(name = "heading")
  public IRubyObject heading(ThreadContext context) {
    return context.getRuntime().newFloat(Math.atan2(jy, jx));
  }
  
  /**
  *
  * @param context
  * @return magnitude float
  */
  @JRubyMethod(name = "mag")
  
  public IRubyObject mag(ThreadContext context) {
    double result = 0;
    if (Math.abs(jx) > EPSILON && Math.abs(jy) > EPSILON) {
      result = Math.hypot(jx, jy);
    }
    else{
      if (Math.abs(jy) > EPSILON) {
        result = Math.abs(jy);
      }
      if (Math.abs(jx) > EPSILON) {
        result = Math.abs(jx);
      }
    }
    return context.getRuntime().newFloat(result);
  }
  
  /**
  * Call yield if block given, do nothing if yield == false else set_mag to
  * given scalar
  *
  * @param context
  * @param scalar double value to set
  * @param block should return a boolean (optional)
  * @return this Vec2D with the new magnitude
  */
  @JRubyMethod(name = "set_mag")
  
  public IRubyObject set_mag(ThreadContext context, IRubyObject scalar, Block block) {
    double new_mag = (Double) scalar.toJava(Double.class);
    if (block.isGiven()) {
      if (!(boolean) block.yield(context, scalar).toJava(Boolean.class)) {
        return this;
      }
    }
    double current = 0;
    if (Math.abs(jx) > EPSILON && Math.abs(jy) > EPSILON) {
      current = Math.hypot(jx, jy);
    }
    else{
      if (Math.abs(jy) > EPSILON) {
        current = Math.abs(jy);
      }
      if (Math.abs(jx) > EPSILON) {
        current = Math.abs(jx);
      }
    }
    if (current > 0) {
      jx *= new_mag / current;
      jy *= new_mag / current;
    }
    return this;
  }
  
  /**
  *
  * @param context
  * @return this as a ruby object
  */
  @JRubyMethod(name = "normalize!")
  
  public IRubyObject normalize_bang(ThreadContext context) {
    double mag = 0;
    if (Math.abs(jx) > EPSILON && Math.abs(jy) > EPSILON) {
      mag = Math.hypot(jx, jy);
    }
    else{
      if (Math.abs(jx) > EPSILON)  {
        mag = Math.abs(jx);
      }
      if (Math.abs(jy) > EPSILON)  {
        mag = Math.abs(jy);
      }
    }
    if (mag > 0) {
      jx /= mag;
      jy /= mag;
    }
    return this;
  }
  
  /**
  *
  * @param context
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "normalize")
  
  public IRubyObject normalize(ThreadContext context) {
    double mag = 0;
    Ruby runtime = context.getRuntime();
    if (Math.abs(jx) > EPSILON && Math.abs(jy) > EPSILON) {
      mag = Math.hypot(jx, jy);
    }
    else{
      if (Math.abs(jx) > EPSILON)  {
        mag = jx;
      }
      if (Math.abs(jy) > EPSILON)  {
        mag = jy;
      }
    }
    if (mag < EPSILON) {
      mag = 1.0;
    }
    return Vec2.rbNew(context, this.getMetaClass(), new IRubyObject[]{
        runtime.newFloat(jx / mag),
        runtime.newFloat(jy / mag)});
  }
  
  /**
  * Example of a regular ruby class method Use Math rather than RadLut
  * here!!!
  *
  * @param context
  * @param klazz
  * @param other input angle in radians
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "from_angle", meta = true)
  public static IRubyObject from_angle(ThreadContext context, IRubyObject klazz, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    double scalar = (Double) other.toJava(Double.class);
    return Vec2.rbNew(context, klazz, new IRubyObject[]{
        runtime.newFloat(Math.cos(scalar)),
        runtime.newFloat(Math.sin(scalar))});
  }
  
  /**
  *
  * @param context
  * @param other
  * @return this Vec2 object rotated
  */
  @JRubyMethod(name = "rotate!")
  public IRubyObject rotate_bang(ThreadContext context, IRubyObject other) {
    double theta = (Double) other.toJava(Double.class);
    double x = (jx * Math.cos(theta) - jy * Math.sin(theta));
    double y = (jx * Math.sin(theta) + jy * Math.cos(theta));
    jx = x;
    jy = y;
    return this;
  }


  
  /**
  *
  * @param context
  * @param other
  * @return a new Vec2 object rotated
  */
  @JRubyMethod(name = "rotate")
  public IRubyObject rotate(ThreadContext context, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    double theta = (Double) other.toJava(Double.class);
    IRubyObject[] ary = new IRubyObject[]{
      runtime.newFloat(jx * Math.cos(theta) - jy * Math.sin(theta)),
      runtime.newFloat(jx * Math.sin(theta) + jy * Math.cos(theta))};
    return Vec2.rbNew(context, this.getMetaClass(), ary);
  }
  
  /**
  *
  * @param context
  * @param args
  * @return as a new Vec2 object (ruby)
  */
  @JRubyMethod(name = "lerp", rest = true)
  public IRubyObject lerp(ThreadContext context, IRubyObject[] args) {
    Ruby runtime = context.getRuntime();
    Arity.checkArgumentCount(runtime, args, 2, 2);
    Vec2 vec = (Vec2) args[0].toJava(Vec2.class);
    double scalar = (Double) args[1].toJava(Double.class);
    assert (scalar >= 0 && scalar < 1.0) :
    "Lerp value " + scalar + " out of range 0 .. 1.0";
    return Vec2.rbNew(context, this.getMetaClass(), new IRubyObject[]{
        runtime.newFloat(jx + (vec.jx - jx) * scalar),
        runtime.newFloat(jy + (vec.jy - jy) * scalar)});
  }
  
  /**
  *
  * @param context
  * @param args
  * @return this
  */
  @JRubyMethod(name = "lerp!", rest = true)
  public IRubyObject lerp_bang(ThreadContext context, IRubyObject[] args) {
    Arity.checkArgumentCount(context.getRuntime(), args, 2, 2);
    Vec2 vec = (Vec2) args[0].toJava(Vec2.class);
    double scalar = (Double) args[1].toJava(Double.class);
    assert (scalar >= 0 && scalar < 1.0) :
    "Lerp value " + scalar + " out of range 0 .. 1.0";
    jx += (vec.jx - jx) * scalar;
    jy += (vec.jy - jy) * scalar;
    return this;
  }
  
  /**
  *
  * @param context
  * @param other
  * @return theta radians float
  */
  @JRubyMethod(name = "angle_between")
  
  public IRubyObject angleBetween(ThreadContext context, IRubyObject other) {
    Vec2 vec = null;
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      vec = (Vec2) other.toJava(Vec2.class);
    } else {
      throw runtime.newTypeError("argument should be Vec2D");
    }
    return runtime.newFloat(Math.atan2(jx - vec.jx, jy - vec.jy));
  }

/**
  * Example of a regular ruby class method Use Math rather than RadLut
  * here!!!
  *
  * @param context
  * @param klazz
  * @return new Vec2 object (ruby)
  */
  @JRubyMethod(name = "random", meta = true)
  public static IRubyObject random_direction(ThreadContext context, IRubyObject klazz) {
    Ruby runtime = context.getRuntime();
    double angle = Math.random() * Math.PI * 2;
    return Vec2.rbNew(context, klazz, new IRubyObject[]{
        runtime.newFloat(Math.cos(angle)),
        runtime.newFloat(Math.sin(angle))});
  }
  
  /**
  *
  * @param context
  * @return new copy
  */
  @JRubyMethod(name = {"copy", "dup"})
  
  public IRubyObject copy(ThreadContext context) {
    Ruby runtime = context.runtime;
    return Vec2.rbNew(context, this.getMetaClass(), new IRubyObject[]{
      runtime.newFloat(jx), 
      runtime.newFloat(jy)});
  }
  
  /**
  *
  * @param context
  * @return ruby array
  */
  @JRubyMethod(name = "to_a")
  
  public IRubyObject toArray(ThreadContext context) {
    Ruby runtime = context.runtime;
    return RubyArray.newArray(context.getRuntime(), new IRubyObject[]{
      runtime.newFloat(jx), 
      runtime.newFloat(jy)});
  }
  
  /**
  *
  * @param context
  * @param object
  */
  @JRubyMethod(name = "to_vertex")
  
  public void toVertex(ThreadContext context, IRubyObject object) {
    JRender renderer = (JRender) object.toJava(JRender.class);
    renderer.vertex(jx, jy);
  }
  
  /**
  *
  * @param context
  * @param object
  */
  @JRubyMethod(name = "to_curve_vertex")
  
  public void toCurveVertex(ThreadContext context, IRubyObject object) {
    JRender renderer = (JRender) object.toJava(JRender.class);
    renderer.curveVertex(jx, jy);
  }
  
  
  /**
  * For jruby-9000 we alias to inspect
  * @param context
  * @return custom to string (inspect)
  */
  @JRubyMethod(name = {"to_s", "inspect"})
  
  public IRubyObject to_s(ThreadContext context) {
    return context.getRuntime().newString(String.format("Vec2D(x = %4.4f, y = %4.4f)", jx, jy));
  }
  
  /**
  *
  * @return hash int
  */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + (int) (Double.doubleToLongBits(this.jx) ^ (Double.doubleToLongBits(this.jx) >>> 32));
    hash = 53 * hash + (int) (Double.doubleToLongBits(this.jy) ^ (Double.doubleToLongBits(this.jy) >>> 32));
    return hash;
  }
  
  /**
  *
  * @param obj
  * @return ruby boolean
  */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Vec2){
      final Vec2 other = (Vec2) obj;
      if (!((Double)this.jx).equals(other.jx)) {
        return false;
      }
      return ((Double)this.jy).equals(other.jy);
    }
    return false;
  }
  
  /**                                                                              
  *                                                                     
  * @param context
  * @param other
  * @return ruby boolean                                                                       
  */                                                                               
  @JRubyMethod(name = "eql?", required = 1)                                                                    
  public IRubyObject eql_p(ThreadContext context, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2){                                                    
      Vec2 v = (Vec2) other.toJava(Vec2.class);                                              
      if (!((Double)this.jx).equals(v.jx)) {                                   
        return RubyBoolean.newBoolean(runtime, false);                                                          
      }                                                                            
      return RubyBoolean.newBoolean(runtime, ((Double)this.jy).equals(v.jy));                                   
    }                                                                            
    return RubyBoolean.newBoolean(runtime, false);                                                                     
  }
  
  /**
  *
  * @param context
  * @param other
  * @return ruby boolean
  */
  @JRubyMethod(name = "==", required = 1)
  
  @Override
  public IRubyObject op_equal(ThreadContext context, IRubyObject other) {
    Ruby runtime = context.getRuntime();
    if (other instanceof Vec2) {
      Vec2 v = (Vec2) other.toJava(Vec2.class);
      double diff = jx - v.jx;
      if ((diff < 0 ? -diff : diff) > Vec2.EPSILON) {
        return RubyBoolean.newBoolean(runtime, false);
      }
      diff = jy - v.jy;
      boolean result = ((diff < 0 ? -diff : diff) < Vec2.EPSILON);
      return RubyBoolean.newBoolean(runtime, result);
    }
    return RubyBoolean.newBoolean(runtime, false);
  }
}
