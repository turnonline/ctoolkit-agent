/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.service.impl.event;

import com.google.common.eventbus.EventBus;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ctoolkit.agent.model.BaseEntity;

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
