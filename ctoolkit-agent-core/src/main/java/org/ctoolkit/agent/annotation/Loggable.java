package org.ctoolkit.agent.annotation;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotations is used to annotate class or method and enable AOP logging around class methods or method
 * <p>
 * Created by Jozef Pohorelec on 21. 10. 2015.
 */
@Target( {ElementType.TYPE, ElementType.METHOD} )
@Retention( RUNTIME )
@Around
@Type(LoggableInterceptor.class)
public @interface Loggable
{
    boolean value() default true;
}