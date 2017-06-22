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
import com.google.common.eventbus.Subscribe;
import org.ctoolkit.agent.service.ChangeSetService;

import javax.inject.Inject;

/**
 * Audit subscription
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuditSubscription
{
    private final ChangeSetService changeSetService;

    @Inject
    public AuditSubscription( EventBus eventBus,
                              ChangeSetService changeSetService )
    {
        eventBus.register( this );

        this.changeSetService = changeSetService;
    }

    @Subscribe
    public void handle( AuditEvent event )
    {
        changeSetService.create( event );
    }
}
