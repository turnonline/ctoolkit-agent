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

package org.ctoolkit.agent.service.impl.datastore;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import org.ctoolkit.agent.service.impl.event.AuditEvent;

import javax.inject.Inject;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class AuditSubscription
{
    private Injector injector;

    @Inject
    public AuditSubscription( EventBus eventBus, Injector injector )
    {
        eventBus.register( this );
        this.injector = injector;
    }

    @Subscribe
    public void handle( AuditEvent event )
    {
        // TODO: refactor to new API


//        RestContext ctx = injector.getInstance( RestContext.class );
//        Entity audit = new Entity( "_MetadataAudit" );
//
//        for ( Map.Entry<String, String> entry : event.entrySet() )
//        {
//            audit.setProperty( entry.getKey(), entry.getValue() );
//        }
//
//        audit.setProperty( "ownerId", event.getOwner().getKey() );
//        audit.setProperty( "action", event.getAction().name() );
//        audit.setProperty( "operation", event.getOperation().name() );
//        audit.setProperty( "createDate", new Date() );
//        audit.setProperty( "createdBy", ctx.getUserEmail() );
//        audit.setProperty( "userPhotoUrl", ctx.getPhotoUrl() );
//        audit.setProperty( "userDisplayName", ctx.getDisplayName() );
//
//        ofy().save().entity( audit );
    }
}
