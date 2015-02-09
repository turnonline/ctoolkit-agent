package org.ctoolkit.agent.dataset.reader.impl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.processor.ChangeSetVersion;
import org.ctoolkit.agent.dataset.reader.ChangeSetVersionService;

import java.util.Date;

/**
 * Class managing the history entries in the database.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class ChangeSetVersionImpl
        implements ChangeSetVersionService
{
    /**
     * This entity is returned when there is no record in the history table
     */
    protected static final ChangeSetVersion noHistory = new ChangeSetVersion();

    private final static String KIND = "VersionInfo";

    private final static String PROPERTY_VERSION = "Version";

    private final static String PROPERTY_CREATED = "Created";

    private final static String PROPERTY_USER = "User";

    @Override
    public ChangeSetVersion getCurrentChangeSetVersion()
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query( KIND ).addSort( PROPERTY_CREATED, SortDirection.DESCENDING );

        PreparedQuery preparedQuery = dataStore.prepare( query );
        QueryResultList<Entity> entList = preparedQuery.asQueryResultList( FetchOptions.Builder.withLimit( 1 ) );
        if ( !entList.isEmpty() )
        {
            return entityToChangeSetVersion( entList.get( 0 ) );
        }

        return noHistory;
    }

    /**
     * Reads version info from the change set.
     *
     * @param changeSet change set, with version
     * @return the change set version bean holding version info
     */
    private ChangeSetVersion extractVersionInfo( ChangeSet changeSet )
    {
        ChangeSetVersion changeSetVersion = new ChangeSetVersion();
        changeSetVersion.setVersion( changeSet.getVersion() );
        changeSetVersion.setUser( changeSet.getAuthor() );

        return changeSetVersion;
    }

    public void saveChangeSetVersionInfo( ChangeSet changeSet )
    {
        saveChangeSetVersionInfo( extractVersionInfo( changeSet ) );
    }

    @Override
    public void saveChangeSetVersionInfo( ChangeSetVersion version )
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        dataStore.put( changeSetVersionToEntity( version ) );
    }


    private ChangeSetVersion entityToChangeSetVersion( Entity entity )
    {
        ChangeSetVersion changeSetVersion = new ChangeSetVersion();
        if ( null != entity )
        {
            changeSetVersion.setVersion( ( Long ) entity.getProperty( PROPERTY_VERSION ) );
            changeSetVersion.setUser( ( String ) entity.getProperty( PROPERTY_USER ) );
            changeSetVersion.setCreated( ( Date ) entity.getProperty( PROPERTY_CREATED ) );
        }

        return changeSetVersion;
    }

    private Entity changeSetVersionToEntity( ChangeSetVersion changeSetVersion )
    {
        if ( null == changeSetVersion )
        {
            return null;
        }
        Entity entity = new Entity( KIND );
        entity.setProperty( PROPERTY_VERSION, changeSetVersion.getVersion() );
        entity.setUnindexedProperty( PROPERTY_USER, changeSetVersion.getUser() );
        entity.setProperty( PROPERTY_CREATED, changeSetVersion.getCreated() );

        return entity;
    }
}
