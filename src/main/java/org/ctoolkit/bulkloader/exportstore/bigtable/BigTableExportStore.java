package org.ctoolkit.bulkloader.exportstore.bigtable;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.common.BulkLoaderConstants;
import org.ctoolkit.bulkloader.exportstore.ChangeSetEntityEncoder;
import org.ctoolkit.bulkloader.exportstore.CompressedChangeSetEntityEncoder;
import org.ctoolkit.bulkloader.exportstore.ExportStore;
import org.ctoolkit.bulkloader.exportstore.XmlChangeSetEntityEncoder;
import org.ctoolkit.bulkloader.utils.DataStoreEntityPool;

import java.util.Date;
import java.util.List;

/**
 * AppEngine implementation of the export store
 * Uses low level AppEngine API to store export data
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BigTableExportStore
        implements ExportStore
{

    private static final String MASTER_EXPORT_TABLE = "ExportStoreMaster";

    private static final String MET_COLUMN_VERSION = "version";

    private static final String MET_COLUMN_SUBVERSION = "subversion";

    private static final String MET_COLUMN_CREATED = "created";

    private static final String MET_COLUMN_STATUS = "status";

    private static final String MET_COLUMN_EXPORT_TYPE = "type";

    private static final String CHILD_EXPORT_TABLE = "ExportStore";

    private static final String CET_COLUMN_DATA = "data";

    private final DatastoreService dataStore;

    private final DataStoreEntityPool dataStoreEntityPool;

    private final ChangeSetEntityEncoder csEntityEncoder;

    private final Long exportType;

    private Key masterExportStoreKey;

    // variables used by iterating trough change sets
    // it holds the value of the current version.subversion to return
    private Long cursorVersion;

    private Long cursorSubVersion;

    private Long maxVersion;

    /**
     * Cursor into ExportStore
     */
    private Cursor cursor;

    /**
     * Default constructor
     */
    public BigTableExportStore()
    {
        dataStore = DatastoreServiceFactory.getDatastoreService();
        csEntityEncoder = new CompressedChangeSetEntityEncoder( new XmlChangeSetEntityEncoder() );
        dataStoreEntityPool = new DataStoreEntityPool( DatastoreServiceFactory.getDatastoreService() );
        exportType = EXPORT_TYPE_FULL_EXPORT;
    }

    /**
     * Method stores change set in the blob store
     *
     * @param changeSet change set to store
     * @return
     */
    @Override
    public boolean saveChangeSet( final ChangeSet changeSet )
    {
        if ( changeSet.hasEntities() )
        {
            for ( ChangeSetEntity csEntity : changeSet.getEntities().getEntities() )
            {
                byte[] encodedEntity = csEntityEncoder.toByteArray( csEntity );
                if ( null == encodedEntity )
                {
                    return false;
                }
                else
                {
                    Entity child = createExportStoreChildRecord( masterExportStoreKey, encodedEntity );
                    dataStoreEntityPool.put( child );
                }
            }
            dataStoreEntityPool.flush();
        }
        return true;
    }

    /**
     * @see org.ctoolkit.bulkloader.exportstore.ExportStore#setFirstChangeSet(java.lang.Long)
     */
    public void setFirstChangeSet( Long maxVersion )
    {
        this.maxVersion = maxVersion;
        // find the first full export with version <= maxVersion
        Query query = new Query( MASTER_EXPORT_TABLE )
                .addSort( MET_COLUMN_VERSION, SortDirection.DESCENDING )
                .addSort( MET_COLUMN_SUBVERSION, SortDirection.DESCENDING )
                .addFilter( MET_COLUMN_STATUS, FilterOperator.EQUAL, EXPORT_STATUS_DONE )
                .addFilter( MET_COLUMN_EXPORT_TYPE, FilterOperator.EQUAL, EXPORT_TYPE_FULL_EXPORT );
        if ( 0 != maxVersion )
        {
            query.addFilter( MET_COLUMN_VERSION, FilterOperator.LESS_THAN_OR_EQUAL, maxVersion );
        }
        PreparedQuery preparedQuery = dataStore.prepare( query );
        QueryResultList<Entity> entList = preparedQuery.asQueryResultList( FetchOptions.Builder.withLimit( 1 ) );
        if ( !entList.isEmpty() )
        {
            // set the internal pointer to this version.subversion
            Entity mrEntity = entList.get( 0 );
            this.cursorVersion = ( Long ) mrEntity.getProperty( MET_COLUMN_VERSION );
            this.cursorSubVersion = ( Long ) mrEntity.getProperty( MET_COLUMN_SUBVERSION );
            this.cursor = null;
        }
        else
        {
        }
    }

    /**
     *
     */
    @Override
    public void setFirstChangeSet( Long maxVersion, Long version, Long subVersion, String cursor )
    {
        this.maxVersion = maxVersion;
        this.cursorVersion = version;
        this.cursorSubVersion = subVersion;
        this.cursor = Cursor.fromWebSafeString( cursor );
    }

    /**
     * @see org.ctoolkit.bulkloader.exportstore.ExportStore#setFirstChangeSet(java.lang.Long)
     */
    boolean findNextChangeSet( Long maxVersion, Long currentVersion, Long currentSubVersion )
    {
        // find the next export chunk with version <= version
        Query query = new Query( MASTER_EXPORT_TABLE )
                .addSort( MET_COLUMN_VERSION, SortDirection.ASCENDING )
                .addSort( MET_COLUMN_SUBVERSION, SortDirection.ASCENDING )
                .addFilter( MET_COLUMN_STATUS, FilterOperator.EQUAL, EXPORT_STATUS_DONE );
        if ( 0 != maxVersion )
        {
            query.addFilter( MET_COLUMN_VERSION, FilterOperator.LESS_THAN_OR_EQUAL, maxVersion );
        }
        else
        {
            query.addFilter( MET_COLUMN_VERSION, FilterOperator.GREATER_THAN_OR_EQUAL, currentVersion );
        }

        // look for the next version.subversion
        PreparedQuery preparedQuery = dataStore.prepare( query );
        Long version;
        Long subVersion;
        QueryResultList<Entity> entList = preparedQuery.asQueryResultList( FetchOptions.Builder.withDefaults() );
        if ( !entList.isEmpty() )
        {
            for ( Entity entity : entList )
            {
                // set the internal pointer to this version.subversion
                version = ( Long ) entity.getProperty( MET_COLUMN_VERSION );
                subVersion = ( Long ) entity.getProperty( MET_COLUMN_SUBVERSION );
                if ( ( ( version.equals( currentVersion ) ) && ( subVersion > currentSubVersion ) ) || ( ( version > currentVersion ) ) )
                {
                    // set the internal pointer to this version.subversion
                    this.cursorVersion = version;
                    this.cursorSubVersion = subVersion;
                    this.cursor = null;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method returns the next ChangeSet. It can be called more than once every
     * time you call the method you get the next change set. If there isn't any
     * other, the return value is null
     *
     * @return ChangeSet or null
     */
    @Override
    public ChangeSet getNextChangeSet()
    {
        ChangeSet changeSet = readChangeSet( cursorVersion, cursorSubVersion, cursor );
        if ( null == changeSet || changeSet.isEmpty() )
        {
            // increment the version.subversion
            // look for the next export store entry
            if ( findNextChangeSet( this.maxVersion, this.cursorVersion, this.cursorSubVersion ) )
            {
                // if there is another record, read it
                return getNextChangeSet();
            }
        }
        return changeSet;
    }

    /**
     *
     */
    @Override
    public String getCursor()
    {
        if ( null != cursor )
        {
            return cursor.toWebSafeString();
        }
        return "";
    }

    /**
     * Read the change set from the data store
     *
     * @param version    version to read
     * @param subVersion subversion to read
     * @param cursor
     * @return
     */
    private ChangeSet readChangeSet( Long version, Long subVersion, Cursor cursor )
    {
        // change set is stored as (MasterExportStoreEntity->
        // get the master record key
        Key masterKey = getFinishedExportStoreMasterRecordKey( version, subVersion );
        if ( null != masterKey )
        {
            // read all the next child record
            // TODO: Move import user name to global config
            ChangeSet changeSet = ChangeSet.createChangeSet( version, subVersion, "importUser" );
            Query childRecordsQuery = new Query( CHILD_EXPORT_TABLE, masterKey );
            // read the entities
            // TODO: implement cursor fetching and bulk reading - limit should be read from global config
            FetchOptions fetchOptions = FetchOptions.Builder.withDefaults().limit( 1 );
            if ( null != cursor )
            {
                fetchOptions.startCursor( cursor );
            }
            QueryResultList<Entity> result = dataStore.prepare( childRecordsQuery ).asQueryResultList( fetchOptions );
            if ( !result.isEmpty() )
            {
                for ( Entity ent : result )
                {
                    changeSet.addEntity( csEntityEncoder.toChangeSetEntity( ( ( Blob ) ent.getProperty( CET_COLUMN_DATA ) ).getBytes() ) );
                }
                this.cursor = result.getCursor();
            }
            return changeSet;
        }
        return null;
    }

    /**
     * Method called when the export procedure starts
     */
    @Override
    public Long beginExport( Long version )
    {
        // generate sub version number
        // get the latest sub version, and increment by 1 - sequence generator
        // TODO: sequence generator to avoid duplicated version.subversion
        long subVersion = ( long ) findMostRecentSubVersion( version ) + 1;
        // create an entry in the export store with version.subversion
        dataStore.put( createExportStoreMasterRecord( version, subVersion ) );
        masterExportStoreKey = getCurrentExportStoreMasterRecordKey( version, subVersion ).getKey();
        return subVersion;
    }

    /**
     * @param version
     * @param subVersion
     */
    @Override
    public boolean continueExport( Long version, Long subVersion )
    {
        Entity currentExportStoreMasterRecordKey = getCurrentExportStoreMasterRecordKey( version, subVersion );
        if ( null == currentExportStoreMasterRecordKey )
        {
            masterExportStoreKey = null;
            return false;
        }
        masterExportStoreKey = currentExportStoreMasterRecordKey.getKey();
        return true;
    }

    /**
     * Export process finished update the status flag
     */
    @Override
    public void finishExport( Long version, Long subVersion )
    {
        Entity currentMasterRecord = getCurrentExportStoreMasterRecordKey( version, subVersion );
        if ( null != currentMasterRecord )
        {
            currentMasterRecord.setProperty( MET_COLUMN_STATUS, ExportStore.EXPORT_STATUS_DONE );
            dataStore.put( currentMasterRecord );
        }
    }

    /**
     * Method looks for the most recent subversion for the given version
     *
     * @param version main version
     * @return latest subversion for the given version, if there is no version
     * it returns 0
     */
    private int findMostRecentSubVersion( Long version )
    {
        Query query = new Query( MASTER_EXPORT_TABLE );
        query.addFilter( MET_COLUMN_VERSION, FilterOperator.EQUAL, version );
        query.addSort( MET_COLUMN_SUBVERSION, SortDirection.DESCENDING );
        PreparedQuery preparedQuery = dataStore.prepare( query );
        List<Entity> entList = preparedQuery.asList( FetchOptions.Builder.withLimit( 1 ) );
        if ( entList.size() > 0 )
        {
            Long sv = ( Long ) entList.get( 0 ).getProperty( MET_COLUMN_SUBVERSION );
            return sv.intValue();
        }
        return 0;
    }

    /**
     * Method creates the main export entity
     *
     * @param version    version number
     * @param subVersion sub version number
     * @return the created entity
     */
    private Entity createExportStoreMasterRecord( Long version, Long subVersion )
    {
        Entity masterExportStoreEntity = new Entity( MASTER_EXPORT_TABLE );
        masterExportStoreEntity.setProperty( MET_COLUMN_VERSION, version );
        masterExportStoreEntity.setProperty( MET_COLUMN_SUBVERSION, subVersion );
        masterExportStoreEntity.setProperty( MET_COLUMN_EXPORT_TYPE, exportType );
        masterExportStoreEntity.setProperty( MET_COLUMN_STATUS, ExportStore.EXPORT_STATUS_EXPORTING );    // work in progress
        masterExportStoreEntity.setProperty( MET_COLUMN_CREATED, new Date() );
        return masterExportStoreEntity;
    }

    /**
     * Looks for the master record for the given export
     *
     * @param version    version to look for
     * @param subVersion subversion to look for
     * @return export master record key
     */
    private Key getFinishedExportStoreMasterRecordKey( Long version, Long subVersion )
    {
        Query query = new Query( MASTER_EXPORT_TABLE )
                .addFilter( MET_COLUMN_VERSION, FilterOperator.EQUAL, version )
                .addFilter( MET_COLUMN_SUBVERSION, FilterOperator.EQUAL, subVersion )
                .addFilter( MET_COLUMN_STATUS, FilterOperator.EQUAL, ExportStore.EXPORT_STATUS_DONE );
        PreparedQuery preparedQuery = dataStore.prepare( query );
        Entity entity = preparedQuery.asSingleEntity();
        if ( null != entity )
        {
            return entity.getKey();
        }
        return null;
    }

    /**
     * Looks for the master record for the given export
     *
     * @param version    version to look for
     * @param subVersion subversion to look for
     * @return export master record key
     */
    private Entity getCurrentExportStoreMasterRecordKey( Long version, Long subVersion )
    {
        Query query = new Query( MASTER_EXPORT_TABLE )
                .addFilter( MET_COLUMN_VERSION, FilterOperator.EQUAL, version )
                .addFilter( MET_COLUMN_SUBVERSION, FilterOperator.EQUAL, subVersion )
                .addFilter( MET_COLUMN_STATUS, FilterOperator.EQUAL, ExportStore.EXPORT_STATUS_EXPORTING );
        PreparedQuery preparedQuery = dataStore.prepare( query );
        return preparedQuery.asSingleEntity();
    }

    /**
     * Method creates child record for the main export store record
     *
     * @param parent identifier of the main entity
     * @param data   data to store
     * @return the created entity
     */
    private Entity createExportStoreChildRecord( Key parent, byte[] data )
    {
        Entity entity = new Entity( CHILD_EXPORT_TABLE, parent );
        entity.setProperty( CET_COLUMN_DATA, new Blob( data ) );
        entity.setUnindexedProperty( MET_COLUMN_CREATED, new Date() );
        return entity;
    }

    /**
     * Method returns basic information about the Export store
     *
     * @return export store information
     */
    public String getInfo()
    {
        StringBuilder info = new StringBuilder();
        Query query = new Query( MASTER_EXPORT_TABLE );
        query.addSort( MET_COLUMN_VERSION, SortDirection.DESCENDING );
        query.addSort( MET_COLUMN_SUBVERSION, SortDirection.DESCENDING );
        PreparedQuery pq = dataStore.prepare( query );
        List<Entity> entList = pq.asList( FetchOptions.Builder.withDefaults() );
        if ( entList.size() > 0 )
        {
            for ( Entity anEntList : entList )
            {
                info.append( versionToString( anEntList ) );
                info.append( BulkLoaderConstants.NEWLINE );
            }
        }
        return info.toString();
    }

    /**
     * @param entity
     * @return
     */
    private String versionToString( Entity entity )
    {
        String type;
        if ( Long.valueOf( EXPORT_TYPE_FULL_EXPORT ).equals( entity.getProperty( MET_COLUMN_EXPORT_TYPE ) ) )
        {
            type = "Full export";
        }
        else if ( Long.valueOf( EXPORT_TYPE_DIFF_EXPORT ).equals( entity.getProperty( MET_COLUMN_EXPORT_TYPE ) ) )
        {
            type = "Differential export";
        }
        else
        {
            type = "? export";
        }
        String status;
        if ( Long.valueOf( EXPORT_STATUS_DONE ).equals( entity.getProperty( MET_COLUMN_STATUS ) ) )
        {
            status = "Ok";
        }
        else if ( Long.valueOf( EXPORT_STATUS_DONE ).equals( entity.getProperty( MET_COLUMN_STATUS ) ) )
        {
            status = "Unfinished";
        }
        else
        {
            status = "Unknown";
        }
        return type
                + ", created on " + entity.getProperty( MET_COLUMN_CREATED )
                + ", version " + entity.getProperty( MET_COLUMN_VERSION ) + "." + entity.getProperty( MET_COLUMN_SUBVERSION )
                + ", status: " + status;
    }
}
