package com.qualcomm.robotcore.eventloop.opmode;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autonomous
{
  String name() default "";
  
  String group() default "";
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\eventloop\opmode\Autonomous.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */