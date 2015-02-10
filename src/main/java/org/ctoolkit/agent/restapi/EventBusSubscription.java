package org.ctoolkit.agent.restapi;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.ctoolkit.agent.dataset.processor.UpgradeCompletedEvent;
import org.ctoolkit.agent.restapi.resource.DataSetUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Common event bus subscription implementation resides in this class.
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class EventBusSubscription
{
    private static final Logger log = LoggerFactory.getLogger( EventBusSubscription.class );

    @Inject
    public EventBusSubscription( EventBus eventBus )
    {
        eventBus.register( this );
    }

    @Subscribe
    public void handleDataSetUpgradeCompleted( UpgradeCompletedEvent event )
    {
        Long id = event.getNotificationId();
        DataSetUpgrade upgrade = ofy().load().type( DataSetUpgrade.class ).id( id ).now();

        if ( upgrade != null )
        {
            upgrade.setCompleted( true );
            upgrade.setCompletedAt( new Date() );

            ofy().save().entity( upgrade ).now();
        }
        else
        {
            log.warn( "DataSetUpgrade instance has not found for Id = " + event.getNotificationId() );
        }

        log.info( "Upgrade has completed: " + upgrade );
    }
}
