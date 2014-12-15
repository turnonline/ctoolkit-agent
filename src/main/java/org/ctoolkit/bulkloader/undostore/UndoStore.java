package org.ctoolkit.bulkloader.undostore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.changesets.model.ChangeSets;
import org.ctoolkit.bulkloader.utils.DataStoreEntityPool;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the Progress Diary. Stores entries in the data source
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class UndoStore
        implements ProgressDiary
{

    /**
     * Logger for this class
     */
    private final static Logger logger = Logger.getLogger( UndoStore.class.getName() );

    private final static String UNDOSTORE_KIND = "UndoStore";

    private final static String UNDOSTORE_PROP_KEY = "Key";

    private final static String UNDOSTORE_PROP_CREATED = "Created";

    private final static String UNDOSTORE_PROP_OPERATION = "Operation";

    /**
     * Method stores the Key of the created entity(ies) in the data store
     */
    @Override
    public void writeEntry( ChangeSet changeSet )
    {
        logger.info( "Writing entry into the UNDO store: " + changeSet );
        if ( changeSet.hasEntities() )
        {
            for ( int i = 0; i < changeSet.getEntities().getEntities().size(); i++ )
            {
                ChangeSetEntity csEnt = changeSet.getEntities().getEntities().get( i );
                saveUndoEntry( csEnt );
            }
        }
    }

    /**
     * Method stores undo entry in the data store
     *
     * @param csEntity changeset entity to store
     */
    private void saveUndoEntry( ChangeSetEntity csEntity )
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Entity undoEntry = new Entity( UNDOSTORE_KIND );
        undoEntry.setUnindexedProperty( UNDOSTORE_PROP_KEY, csEntity.getKey() );
        undoEntry.setUnindexedProperty( UNDOSTORE_PROP_OPERATION, csEntity.getOp() );
        undoEntry.setProperty( UNDOSTORE_PROP_CREATED, new Date() );
        dataStore.put( undoEntry );
    }

    /**
     * @see ProgressDiary#readEntries()
     */
    @Override
    public ChangeSets readEntries()
    {
        logger.info( "Reading entries from the UNDO store" );
        return new ChangeSets();
    }

    /**
     * Remove entries from the undo store
     */
    @Override
    public void purgeEntries()
    {
        logger.info( "Removing entries from the UNDO store" );
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        DataStoreEntityPool dsEntityPool = new DataStoreEntityPool( dataStore );
        while ( true )
        {
            Query query = new Query( UNDOSTORE_KIND );
            List<Entity> entityList = dataStore.prepare( query ).asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( entityList.size() > 0 )
            {
                for ( Entity ent : entityList )
                {
                    dsEntityPool.delete( ent.getKey() );
                }
                dsEntityPool.flush();
            }
            else
            {
                break;
            }
        }
    }

    @Override
    public void rollback()
    {
        logger.info( "Removing entries from the UNDO store" );
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        DataStoreEntityPool dsEntityPool = new DataStoreEntityPool( dataStore );
        while ( true )
        {
            Query query = new Query( UNDOSTORE_KIND );
            query.addSort( UNDOSTORE_PROP_CREATED, SortDirection.DESCENDING );
            // TODO: move constants to config file
            List<Entity> entityList = dataStore.prepare( query ).asList( FetchOptions.Builder.withLimit( 100 ) );
            if ( entityList.size() > 0 )
            {
                for ( Entity ent : entityList )
                {
                    // remove the referred entry
                    dsEntityPool.delete( KeyFactory.stringToKey( ( String ) ent.getProperty( UNDOSTORE_PROP_KEY ) ) );
                    // remove the entry from the undo store
                    dsEntityPool.delete( ent.getKey() );
                }
                dsEntityPool.flush();
            }
            else
            {
                break;
            }
        }
    }

    @Override
    public boolean isEmpty()
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query( UNDOSTORE_KIND );
        List<Entity> entityList = dataStore.prepare( query ).asList( FetchOptions.Builder.withLimit( 1 ) );
        return 0 == entityList.size();
    }

    /**
     * Method returns basic information about the Export store
     *
     * @return export store information
     */
    public String getInfo()
    {
        DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query( UNDOSTORE_KIND );
        query.addSort( UNDOSTORE_PROP_CREATED, SortDirection.DESCENDING );
        PreparedQuery prepQuery = dataStore.prepare( query );
        List<Entity> entityList = prepQuery.asList( FetchOptions.Builder.withLimit( 1 ) );
        if ( entityList.size() > 0 )
        {
            return "The UNDO store is NOT empty, last entry is from " + entityList.get( 0 ).getProperty( UNDOSTORE_PROP_CREATED );
        }
        return "The UNDO store is empty";
    }
}
