package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.common.eventbus.EventBus;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ctoolkit.migration.agent.model.BaseEntity;
import org.ctoolkit.migration.agent.service.impl.event.AuditEvent;
import org.ctoolkit.migration.agent.service.impl.event.Auditable;

import javax.inject.Inject;

/**
 * Audit interceptor
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuditInterceptor
        implements MethodInterceptor
{
    @Inject
    private EventBus eventBus;

    @Override
    public Object invoke( MethodInvocation invocation ) throws Throwable
    {
        Object proceed = invocation.proceed();

        BaseEntity owner = ( BaseEntity ) invocation.getArguments()[0];
        Auditable auditable = invocation.getMethod().getAnnotation( Auditable.class );

        eventBus.post( new AuditEvent( auditable.action(), owner ) );

        return proceed;
    }
}
