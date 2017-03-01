package org.ctoolkit.agent.rest;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Qualifier
@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface Authorized
{
}
