package org.ctoolkit.migration.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mark map reduce MapSpecification as export job specification
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Target( {ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@BindingAnnotation
public @interface ExportJob
{
}
