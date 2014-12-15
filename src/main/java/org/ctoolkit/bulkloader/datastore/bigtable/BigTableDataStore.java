package org.ctoolkit.bulkloader.datastore.bigtable;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.common.BulkLoaderConstants;
import org.ctoolkit.bulkloader.common.BulkLoaderException;
import org.ctoolkit.bulkloader.conf.Configuration;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.conf.model.Kind;
import org.ctoolkit.bulkloader.datastore.AbstractDataStore;
import org.ctoolkit.bulkloader.utils.DataStoreEntityPool;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

/**
 * AppEngine implementation of the DataStore
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BigTableDataStore
        extends AbstractDataStore
{

    /**
     * Logger for this class
     */
    private final Logger logger;

    /**
     * Datastore to work on
     */
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /**
     * Entity pool for bulk processing
     */
    private final DataStoreEntityPool entityPool;

    /**
     * Global configuration data
     */
    private final Configuration configuration;

    /**
     * Encoder converting change sets to data store entities vice versa
     */
    private final BigTableEntityEncoder encoder;

    private String kind;

    private Cursor cursor;

    /**
     * Default constructor
     *
     * @param configuration
     * @param logger
     */
    @Inject
    protected BigTableDataStore( Configuration configuration, Logger logger )
    {
        this.configuration = configuration;
        this.logger = logger;

        entityPool = new DataStoreEntityPool( datastore );
        encoder = new BigTableEntityEncoder();
    }

    @Override
    public void startUpdate()
    {
        logger.info( "Starting data store upgrading" );
    }

    @Override
    protected void cleanKindTable( String kind )
    {
        // cleaning table is the same as dropping it
        dropKindTable( kind );
    }

    @Override
    protected void dropKindTable( String kind )
    {
        logger.info( "Dropping kind '" + kind + "'" );
        int numItems = 0;
        // remove all entities with given kind
        while ( true )
        {
            Query query = new Query( kind ).setKeysOnly();
            PreparedQuery preparedQuery = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = preparedQuery.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    numItems++;
                    entityPool.delete( entity.getKey() );
                }
                entityPool.flush();
            }
            else
            {
                break;
            }
        }
        logger.info( numItems + " '" + kind + "' successfully removed" );
    }

    @Override
    protected void addKindProperty( String kind, String property, String type, String defVal )
            throws BulkLoaderException
    {
        logger.info( "Updating kind '" + kind + "', adding property '" + property + "' with type " + type + " and with default value " + defVal );
        int numItems = changeKindProperty( kind, property, type, defVal );
        logger.info( numItems + " '" + kind + "' successfully added" );
    }

    @Override
    protected void updateKindPropertyValue( String kind, String property, String type, String defVal )
            throws BulkLoaderException
    {
        logger.info( "Updating kind '" + kind + "' property '" + property + "' to " + defVal );
        int numItems = changeKindProperty( kind, property, type, defVal );
        logger.info( numItems + " '" + kind + "' successfully updated" );
    }

    @Override
    protected void removeKindProperty( String kind, String property )
    {
        logger.info( "Removing kind '" + kind + "' property '" + property + "'" );
        int numItems = 0;
        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery pq = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = pq.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    entity.removeProperty( property );
                    entityPool.put( entity );
                    numItems++;
                }
            }
            else
            {
                break;
            }
        }
        entityPool.flush();
        logger.info( numItems + " '" + kind + "' successfully updated" );
    }

    /**
     * Method sets property of the entity to the given type/value
     *
     * @param kind     kind of the entity
     * @param property name of the property
     * @param type     type of the property
     * @param value    value of the property
     * @return number of modified entities
     * @throws BulkLoaderException
     */
    private int changeKindProperty( String kind, String property, String type, String value ) throws BulkLoaderException
    {
        int entitiesChanged = 0;
        while ( true )
        {
            Query query = new Query( kind );
            PreparedQuery prepQuery = datastore.prepare( query );
            // TODO: move fetch option limit to configuration
            List<Entity> entList = prepQuery.asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( !entList.isEmpty() )
            {
                for ( Entity entity : entList )
                {
                    entity.setProperty( property, encoder.encodeProperty( type, value ) );
                    entityPool.put( entity );
                    entitiesChanged++;
                }
            }
            else
            {
                break;
            }
        }
        entityPool.flush();
        return entitiesChanged;
    }

    @Override
    protected void removeEntity( String key )
    {
        logger.info( "Deleting entity from datastore: " + key );
        entityPool.delete( KeyFactory.stringToKey( key ) );
        entityPool.flush();
    }

    @Override
    protected ChangeSetEntity addEntity( ChangeSetEntity csEntity ) throws BulkLoaderException
    {
        logger.info( "Putting entity into datastore: " + csEntity );
        Entity dsEntity = encoder.toDataStoreEntity( csEntity );
        Key key = datastore.put( dsEntity );
        ChangeSetEntity changeSetEntity = new ChangeSetEntity( csEntity, false /*do not need the properties*/ );
        changeSetEntity.setKey( KeyFactory.keyToString( key ) );
        return changeSetEntity;
    }

    @Override
    public ChangeSet getCurrentVersion()
    {
        logger.info( "Starting full diff" );

        // get the data store
        DatastoreService db = DatastoreServiceFactory.getDatastoreService();

        // export_job to execute - if any
        String export_job = ( String ) configuration.getProperty( BulkLoaderConstants.PARAM_EXPORT_JOB );

        logger.info( "Export started" );

        // build an empty changeset bean
        ChangeSet changeSet = ChangeSet.createChangeSet();
        changeSet.setCompleteDataSet( Boolean.FALSE );

        try
        {
            int nrOfEntitiesRead = 0;
            int totalNrOfEntitiesRead = 0;
            for ( ExportJob exportJob : configuration.getExportJobs().getJobs() )
            {
                logger.info( "Checking export job '" + exportJob.getName() + "'" );
                // if there is no specific export_job
                // all of them going to be executed
                if ( null != export_job )
                {
                    // check if export should be executed
                    if ( !export_job.equalsIgnoreCase( exportJob.getName() ) )
                    {
                        // doesn't match, skip this job
                        logger.info( "Skipping export job '" + exportJob.getName() + "'" );
                        continue;
                    }
                }

                logger.info( "Executing export job '" + exportJob.getName() + "'" );
                // execute the export_job
                for ( Kind kind : exportJob.getKinds().getKinds() )
                {
                    // export the current Kind
                    logger.info( "Exporting kind '" + kind.getName() + "'" );
                    Query query = new Query( kind.getName() );
                    for ( final Entity dsEntity : db.prepare( query ).asIterable() )
                    {
                        ChangeSetEntity csEnt = encoder.toChangeSetEntity( dsEntity );
                        // add to change set
                        changeSet.addEntity( csEnt );
                        ++nrOfEntitiesRead;
                    }
                    totalNrOfEntitiesRead += nrOfEntitiesRead;
                    logger.info( "Exporting kind '" + kind.getName() + "' done, " + nrOfEntitiesRead + " entity read." );
                    nrOfEntitiesRead = 0;
                }
            }

            logger.info( "Export jobs done, " + totalNrOfEntitiesRead + " entities read" );
            return changeSet;
        }
        catch ( Exception e )
        {
            logger.severe( "Problem by exporting entities: " + e.getMessage() );
        }
        return null;
    }

    @Override
    public void setFirstChangeSet( String kind, String cursor )
    {
        this.kind = kind;
        if ( null != cursor )
        {
            this.cursor = Cursor.fromWebSafeString( cursor );
        }
    }

    @Override
    public ChangeSet getNextChangeSet()
    {
        // create empty, default changeset
        ChangeSet changeSet = ChangeSet.createChangeSet();
        if ( null != kind )
        {
            Query query = new Query( kind );
            // read the entities
            // TODO: implement cursor fetching and bulk reading - limit should be read from global config
            FetchOptions fetchOptions = FetchOptions.Builder.withDefaults().limit( 1 );
            if ( null != cursor )
            {
                fetchOptions.startCursor( cursor );
            }
            // get the data store
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            QueryResultList<Entity> result = datastoreService.prepare( query ).asQueryResultList( fetchOptions );
            if ( !result.isEmpty() )
            {
                for ( Entity entity : result )
                {
                    changeSet.addEntity( encoder.toChangeSetEntity( entity ) );
                }
                this.cursor = result.getCursor();
            }
            else
            {
                this.cursor = null;
            }
        }

        return changeSet;
    }


    @Override
    public String getCursor()
    {
        return null == cursor ? null : cursor.toWebSafeString();
    }

    @Override
    public String getInfo()
    {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endUpdate()
    {
        logger.info( "Data store upgrading done" );
        entityPool.flush();
    }
}
