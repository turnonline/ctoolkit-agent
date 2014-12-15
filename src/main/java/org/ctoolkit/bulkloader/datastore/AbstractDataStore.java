package org.ctoolkit.bulkloader.datastore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntities;
import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.changesets.model.KindOp;
import org.ctoolkit.bulkloader.changesets.model.KindPropOp;
import org.ctoolkit.bulkloader.common.BulkLoaderException;

import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract methods, which must be implemented for every data store
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public abstract class AbstractDataStore
        implements DataStore
{

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger( AbstractDataStore.class.getName() );

    /**
     * Callback for data store operations
     */
    // TODO: implement as observer pattern?
    private DataStoreCallback datastoreCallback = null;

    @Override
    public void registerCallback( DataStoreCallback dataStoreCallback )
    {
        this.datastoreCallback = dataStoreCallback;
    }

    /**
     * Method called before the first execute method is called Can be used for
     * starting transactions etc.
     *
     * @return
     */
    protected abstract void startUpdate();

    /**
     * Method removes all entries from the kind table
     *
     * @param kind kind table to clean
     */
    protected abstract void cleanKindTable( String kind );

    /**
     * Method removes the kind from the data store
     *
     * @param kind Kind to remove
     */
    protected abstract void dropKindTable( String kind );

    /**
     * Method adds a property with given property to the kind
     *
     * @param kind     kind to modify
     * @param property name of the property to add
     * @param type     type of the property
     * @param defVal   default value of the property
     * @throws org.ctoolkit.bulkloader.common.BulkLoaderException
     */
    protected abstract void addKindProperty( String kind, String property, String type, String defVal )
            throws BulkLoaderException;

    /**
     * Method removes a property from the kind
     *
     * @param kind     kind to modify
     * @param property name of the property to remove
     */
    protected abstract void removeKindProperty( String kind, String property );

    /**
     * Method upgrades a property value in kind
     *
     * @param kind     kind to modify
     * @param property name of the property to add
     * @param type     type of the property
     * @param newValue new value of the property
     * @throws BulkLoaderException
     */
    protected abstract void updateKindPropertyValue( String kind, String property, String type, String newValue )
            throws BulkLoaderException;

    /**
     * Method removes an entity with given key from the data source
     *
     * @param key entity identifier
     */
    protected abstract void removeEntity( String key );

    /**
     * Method adds an entity to the data store
     *
     * @param entity entity description to add
     * @return Change set entity with set identifier
     */
    protected abstract ChangeSetEntity addEntity( ChangeSetEntity entity ) throws BulkLoaderException;

    /**
     * Method called after the last execute operation.
     *
     * @return
     */
    protected abstract void endUpdate();

    /**
     * Method applies change set on data store
     *
     * @param changeSet change set to apply
     */
    public void applyChangeSet( final ChangeSet changeSet ) throws BulkLoaderException
    {
        // fire change set update event
        startUpdate();

        // apply model changes
        if ( changeSet.hasModel() )
        {
            if ( changeSet.getModel().hasKindOps() )
            {
                executeEntityKindOps( changeSet.getModel().getKindOps() );
            }
            if ( changeSet.getModel().hasKindPropOps() )
            {
                executeEntityKindPropOps( changeSet.getModel().getKindPropOps() );
            }
        }

        // apply entity changes
        if ( changeSet.hasEntities() )
        {
            executeEntityOps( changeSet.getEntities() );
        }

        // fire end update event
        endUpdate();
    }

    /**
     * Execute entity kind operation
     *
     * @param kindOps kind operations to execute
     * @throws BulkLoaderException
     */
    private void executeEntityKindOps( List<KindOp> kindOps ) throws BulkLoaderException
    {
        for ( KindOp kindOp : kindOps )
        {
            executeKindOp( kindOp );
            // TODO: callback for entity kind operation?
        }
    }

    /**
     * Execute entity property operation
     *
     * @param kindPropOps kind property operations to execute
     * @throws BulkLoaderException
     */
    private void executeEntityKindPropOps( List<KindPropOp> kindPropOps ) throws BulkLoaderException
    {
        for ( KindPropOp kindPropOp : kindPropOps )
        {
            executeKindPropOp( kindPropOp );
            // TODO: callback for enity property operation
        }
    }

    /**
     * Execute entity operation
     *
     * @param entities entities to execute
     * @throws BulkLoaderException
     */
    private void executeEntityOps( final ChangeSetEntities entities ) throws BulkLoaderException
    {
        for ( ChangeSetEntity cse : entities.getEntities() )
        {
            ChangeSetEntity ret = executeEntityOp( cse );
            if ( null != ret )
            {
                fireEntityOpDoneCallback( ret );
            }
        }
    }

    /**
     * Execute an entity operation
     *
     * @param entityOp entity operation to execute
     * @return
     * @throws BulkLoaderException
     */
    private ChangeSetEntity executeEntityOp( final ChangeSetEntity entityOp ) throws BulkLoaderException
    {
        if ( ChangeSetEntity.OP_REMOVE.equals( entityOp.getOp() ) )
        {
            if ( null != entityOp.getKey() )
            {
                // TODO: call data store callback for removed items
                removeEntity( entityOp.getKey() );
            }
            else
            {
                // key must be specified
                logger.severe( "Missing key for entity delete operation!" );
            }
        }
        else if ( null == entityOp.getOp() )
        {
            return addEntity( entityOp );
        }
        return null;
    }

    /**
     * Execute a kindOp descriptor
     *
     * @param kindOp kind operation to execute
     * @return
     */
    private void executeKindOp( final KindOp kindOp )
    {
        if ( KindOp.OP_DROP.equals( kindOp.getOp() ) )
        {
            dropKindTable( kindOp.getKind() );
        }
        else if ( KindOp.OP_CLEAN.equals( kindOp.getOp() ) )
        {
            cleanKindTable( kindOp.getKind() );
        }
        else
        {
            logger.severe( "Unsupported Kind operation! " + kindOp.getOp() );
        }
    }

    /**
     * Execute a Kind property operation
     *
     * @param kindPropOp kind property operation
     * @return
     * @throws BulkLoaderException
     */
    private void executeKindPropOp( final KindPropOp kindPropOp ) throws BulkLoaderException
    {
        if ( KindPropOp.OP_ADD.equals( kindPropOp.getOp() ) )
        {
            addKindProperty( kindPropOp.getKind(), kindPropOp.getProperty(), kindPropOp.getType(), kindPropOp.getDefVal() );
        }
        else if ( KindPropOp.OP_REMOVE.equals( kindPropOp.getOp() ) )
        {
            removeKindProperty( kindPropOp.getKind(), kindPropOp.getProperty() );
        }
        else if ( KindPropOp.OP_UPDATE.equals( kindPropOp.getOp() ) )
        {
            updateKindPropertyValue( kindPropOp.getKind(), kindPropOp.getProperty(), kindPropOp.getType(), kindPropOp.getDefVal() );
        }
        else if ( KindPropOp.OP_CHANGE.equals( kindPropOp.getOp() ) )
        {
            logger.severe( "Changing properties is not supported yet! " );
        }
        else
        {
            logger.severe( "Unsupported Kind property operation! " + kindPropOp.getOp() );
        }
    }

    /**
     * Call the registered callback handler
     *
     * @param ret
     */
    private void fireEntityOpDoneCallback( final ChangeSetEntity ret )
    {
        if ( null != datastoreCallback )
        {
            datastoreCallback.entityOpDone( ret );
        }
    }
}
