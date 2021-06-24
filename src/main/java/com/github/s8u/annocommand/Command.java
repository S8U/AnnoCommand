package com.github.s8u.annocommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String parent() default "";
    String[] name();

    String arguments() default "";
    String description() default "";

    String permission() default "";

    boolean helpCommand() default false;

}
