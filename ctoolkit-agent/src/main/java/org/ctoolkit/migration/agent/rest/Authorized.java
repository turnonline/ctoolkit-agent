package org.ctoolkit.migration.agent.rest;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Qualifier
@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface Authorized
{
}
