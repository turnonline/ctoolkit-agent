package org.ctoolkit.bulkloader.changesets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetVersion;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Class managing the histories in the database
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ChangeSetVersionImpl
        implements ChangeSetVersionService
{

    /**
     * Logger for this class
     */
    protected static final Logger log = Logger.getLogger( ChangeSetVersionImpl.class.getSimpleName() );

    /**
     * This entity is returned when there is no record in the history table
     */
    protected static final ChangeSetVersion noHistory = new ChangeSetVersion();

    private final static String CHANGESETVERSION_KIND = "VersionInfo";

    private final static String CHANGESETVERSION_PROPERTY_VERSION = "Version";

    private final static String CHANGESETVERSION_PROPERTY_CREATED = "Created";

    private final static String CHANGESETVERSION_PROPERTY_USER = "User";

    /**
     * Method return the current version of the data model
     */
    @Override
    public ChangeSetVersion getCurrentChangeSetVersion()
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query( CHANGESETVERSION_KIND )
                .addSort( CHANGESETVERSION_PROPERTY_CREATED, SortDirection.DESCENDING );

        PreparedQuery preparedQuery = dataStore.prepare( query );
        QueryResultList<Entity> entList = preparedQuery.asQueryResultList( FetchOptions.Builder.withLimit( 1 ) );
        if ( !entList.isEmpty() )
        {
            return entityToChangeSetVersion( entList.get( 0 ) );
        }
        return noHistory;
    }

    /**
     * Method reads version info from the change set
     *
     * @param changeSet change set, with version
     * @return ChangesetVersion bean holding version info
     */
    private ChangeSetVersion extractVersionInfo( ChangeSet changeSet )
    {
        ChangeSetVersion changeSetVersion = new ChangeSetVersion();
        changeSetVersion.setVersion( changeSet.getVersion() );
        changeSetVersion.setUser( changeSet.getAuthor() );
        return changeSetVersion;
    }

    /**
     * Method updates the data model version
     */
    public void saveChangeSetVersionInfo( ChangeSet changeSet )
    {
        saveChangeSetVersionInfo( extractVersionInfo( changeSet ) );
    }

    /**
     * Method updates the data model version It also updates the time stamp in
     * the version info bean
     */
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
            changeSetVersion.setVersion( ( Long ) entity.getProperty( CHANGESETVERSION_PROPERTY_VERSION ) );
            changeSetVersion.setUser( ( String ) entity.getProperty( CHANGESETVERSION_PROPERTY_USER ) );
            changeSetVersion.setCreated( ( Date ) entity.getProperty( CHANGESETVERSION_PROPERTY_CREATED ) );
        }
        return changeSetVersion;
    }

    private Entity changeSetVersionToEntity( ChangeSetVersion changeSetVersion )
    {
        if ( null == changeSetVersion )
        {
            return null;
        }
        Entity entity = new Entity( CHANGESETVERSION_KIND );
        entity.setProperty( CHANGESETVERSION_PROPERTY_VERSION, changeSetVersion.getVersion() );
        entity.setUnindexedProperty( CHANGESETVERSION_PROPERTY_USER, changeSetVersion.getUser() );
        entity.setProperty( CHANGESETVERSION_PROPERTY_CREATED, changeSetVersion.getCreated() );
        return entity;
    }
}
