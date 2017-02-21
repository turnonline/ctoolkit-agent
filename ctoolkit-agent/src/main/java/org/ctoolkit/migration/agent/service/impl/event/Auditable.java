package org.ctoolkit.migration.agent.service.impl.event;

import org.ctoolkit.migration.agent.model.MetadataAudit.Action;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to enable audit on method
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Qualifier
@Target( {ElementType.TYPE, ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
public @interface Auditable
{
    Action action();
}
