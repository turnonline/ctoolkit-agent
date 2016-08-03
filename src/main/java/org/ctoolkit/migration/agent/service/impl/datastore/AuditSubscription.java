package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.ctoolkit.migration.agent.service.impl.event.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AuditSubscription
{
    private Logger logger = LoggerFactory.getLogger( AuditSubscription.class );

    @Inject
    public AuditSubscription( EventBus eventBus )
    {
        eventBus.register( this );
    }

    @Subscribe
    public void handle( AuditEvent event )
    {
        logger.info( "AUDIT: " + event.toString() );

        // TODO: consult with Aurel if required
        /**
         Entity audit = new Entity("_Audit");

         for ( Map.Entry<String, String> entry : event.entrySet() )
         {
         audit.setProperty( entry.getKey(), entry.getValue() );
         }

         audit.setProperty( "metadataId", null ); // TODO: take from map reduce context
         audit.setProperty( "phase", event.getPhase() );
         audit.setProperty( "action", event.getAction() );
         audit.setProperty( "operation", event.getOperation() );
         audit.setProperty( "createDate", new Date() );

         ofy().save().entity( audit ).now();
         */
    }
}
