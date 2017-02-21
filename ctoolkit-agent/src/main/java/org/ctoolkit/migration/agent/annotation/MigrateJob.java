package org.ctoolkit.migration.agent.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mark map reduce MapSpecification as migration job specification
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Target( {ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@BindingAnnotation
public @interface MigrateJob
{
}
