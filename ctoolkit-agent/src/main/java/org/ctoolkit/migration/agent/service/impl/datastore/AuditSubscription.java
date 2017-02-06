package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import org.ctoolkit.migration.agent.service.RestContext;
import org.ctoolkit.migration.agent.service.impl.event.AuditEvent;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
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
        Entity audit = new Entity("_MetadataAudit");

        for ( Map.Entry<String, String> entry : event.entrySet() )
        {
            audit.setProperty( entry.getKey(), entry.getValue() );
        }

        audit.setProperty( "ownerId", event.getOwner().getKey() );
        audit.setProperty( "action", event.getAction().name() );
        audit.setProperty( "operation", event.getOperation().name() );
        audit.setProperty( "createDate", new Date() );
        audit.setProperty( "createdBy", injector.getInstance( RestContext.class ).getUserEmail() );

        ofy().save().entity( audit );
    }
}
