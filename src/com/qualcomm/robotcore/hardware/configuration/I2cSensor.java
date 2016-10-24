package com.qualcomm.robotcore.hardware.configuration;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface I2cSensor
{
  String xmlTag() default "";
  
  String name() default "";
  
  String description() default "an I2c sensor";
}


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\com\qualcomm\robotcore\hardware\configuration\I2cSensor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */