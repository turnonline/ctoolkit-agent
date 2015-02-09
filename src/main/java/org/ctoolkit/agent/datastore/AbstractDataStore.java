package org.ctoolkit.agent.datastore;


import com.googlecode.objectify.ObjectifyService;
import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.ChangeSetEntities;
import org.ctoolkit.agent.dataset.ChangeSetEntity;
import org.ctoolkit.agent.dataset.KindOp;
import org.ctoolkit.agent.dataset.KindPropOp;
import org.ctoolkit.agent.dataset.processor.DataSet;
import org.ctoolkit.agent.dataset.reader.impl.DataSetMultipleXmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The functionality implementation same for all underlying datastores.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public abstract class AbstractDataStore
        implements DataStore
{
    private static final Logger logger = LoggerFactory.getLogger( AbstractDataStore.class );

    private DataStoreCallback datastoreCallback = null;

    @Override
    public void registerCallback( DataStoreCallback callback )
    {
        this.datastoreCallback = callback;
    }

    /**
     * This method is called right before the change set is being processed.
     */
    protected abstract void startUpdate();

    /**
     * Removes all entries from the entity of given kind.
     *
     * @param kind the entity kind to be cleared
     */
    protected abstract void clearEntityKind( String kind );

    /**
     * Removes the entity kind from the data store.
     *
     * @param kind the entity kind to be removed
     */
    protected abstract void dropEntityKind( String kind );

    /**
     * Adds a property with given type to the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be added
     * @param type     the type of the property
     * @param defVal   the default value of the property
     * @throws AgentException
     */
    protected abstract void addEntityProperty( String kind, String property, String type, String defVal )
            throws AgentException;

    /**
     * Removes a property from the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be removed
     */
    protected abstract void removeEntityProperty( String kind, String property );

    /**
     * Upgrades a property value for given entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be added
     * @param type     the type of the property
     * @param newValue the new value of the property
     * @throws AgentException
     */
    protected abstract void updateEntityPropertyValue( String kind, String property, String type, String newValue )
            throws AgentException;

    /**
     * Removes an entity with given key from the data source.
     *
     * @param key the entity unique identifier
     */
    protected abstract void removeEntity( String key );

    /**
     * Adds an entity described by change set to the data store.
     *
     * @param entity the entity to be added
     * @return the change set entity with assigned identifier
     */
    protected abstract ChangeSetEntity addEntity( ChangeSetEntity entity ) throws AgentException;

    @Override
    public DataSet getDataSet( Long id )
    {
        checkNotNull( id );

        DataSet dataSet;

        if ( id == 0 )
        {
            dataSet = new DataSet( 0L );
            dataSet.setPattern( DataSetMultipleXmlReader.FILE_NAME_PATTERN );
            dataSet.setSource( DataSet.Source.LOCAL );
        }
        else
        {
            dataSet = ObjectifyService.ofy().load().type( DataSet.class ).id( id ).now();
        }

        return dataSet;
    }

    /**
     * This method is called right after the change set has been processed.
     */
    protected abstract void endUpdate();

    @Override
    public void applyChangeSet( final ChangeSet changeSet ) throws AgentException
    {
        // fire change set update event
        startUpdate();

        // apply model changes
        if ( changeSet.hasModel() )
        {
            if ( changeSet.getModel().hasKindOps() )
            {
                executeEntityKindOperations( changeSet.getModel().getKindOps() );
            }
            if ( changeSet.getModel().hasKindPropOps() )
            {
                executeEntityKindPropOperations( changeSet.getModel().getKindPropOps() );
            }
        }

        // apply entity changes
        if ( changeSet.hasEntities() )
        {
            executeEntityOperations( changeSet.getEntities() );
        }

        // fire end update event
        endUpdate();
    }

    /**
     * Executes the entity kind operations.
     *
     * @param operations the kind operations to be executed
     * @throws AgentException
     */
    private void executeEntityKindOperations( List<KindOp> operations ) throws AgentException
    {
        for ( KindOp kindOp : operations )
        {
            executeKindOperation( kindOp );
            // TODO: callback for entity kind operation?
        }
    }

    /**
     * Executes the entity property operations.
     *
     * @param operations the entity kind property operations to be executed
     * @throws AgentException
     */
    private void executeEntityKindPropOperations( List<KindPropOp> operations ) throws AgentException
    {
        for ( KindPropOp kindPropOp : operations )
        {
            executeEntityKindPropOperation( kindPropOp );
            // TODO: callback for entity property operation
        }
    }

    /**
     * Executes the entity operation.
     *
     * @param entities entities to be executed
     * @throws AgentException
     */
    private void executeEntityOperations( final ChangeSetEntities entities ) throws AgentException
    {
        for ( ChangeSetEntity cse : entities.getEntities() )
        {
            ChangeSetEntity ret = executeEntityOperation( cse );
            if ( null != ret )
            {
                fireEntityOperationDoneEvent( ret );
            }
        }
    }

    /**
     * Executes an entity operation.
     *
     * @param entityOperation the entity operation to be executed
     * @return the change set entity instance
     * @throws AgentException
     */
    private ChangeSetEntity executeEntityOperation( final ChangeSetEntity entityOperation ) throws AgentException
    {
        if ( ChangeSetEntity.OP_REMOVE.equals( entityOperation.getOperation() ) )
        {
            if ( null != entityOperation.getKey() )
            {
                // TODO: call data store callback for removed items
                removeEntity( entityOperation.getKey() );
            }
            else
            {
                // key must be specified
                logger.error( "Missing key for entity delete operation!" );
            }
        }
        else if ( null == entityOperation.getOperation() )
        {
            return addEntity( entityOperation );
        }
        return null;
    }

    /**
     * Executes an entity kind operation descriptor.
     *
     * @param kindOperation the entity kind operation to be executed
     */
    private void executeKindOperation( final KindOp kindOperation )
    {
        if ( KindOp.OP_DROP.equals( kindOperation.getOperation() ) )
        {
            dropEntityKind( kindOperation.getKind() );
        }
        else if ( KindOp.OP_CLEAN.equals( kindOperation.getOperation() ) )
        {
            clearEntityKind( kindOperation.getKind() );
        }
        else
        {
            logger.error( "Unsupported Kind operation! " + kindOperation.getOperation() );
        }
    }

    /**
     * Executes an entity kind property operation.
     *
     * @param operation the entity kind property operation to be executed
     * @throws AgentException
     */
    private void executeEntityKindPropOperation( final KindPropOp operation ) throws AgentException
    {
        if ( KindPropOp.OP_ADD.equals( operation.getOperation() ) )
        {
            addEntityProperty( operation.getKind(), operation.getProperty(), operation.getType(), operation.getDefVal() );
        }
        else if ( KindPropOp.OP_REMOVE.equals( operation.getOperation() ) )
        {
            removeEntityProperty( operation.getKind(), operation.getProperty() );
        }
        else if ( KindPropOp.OP_UPDATE.equals( operation.getOperation() ) )
        {
            updateEntityPropertyValue( operation.getKind(), operation.getProperty(), operation.getType(), operation.getDefVal() );
        }
        else if ( KindPropOp.OP_CHANGE.equals( operation.getOperation() ) )
        {
            logger.error( "Changing properties is not supported yet! " );
        }
        else
        {
            logger.error( "Unsupported Kind property operation! " + operation.getOperation() );
        }
    }

    private void fireEntityOperationDoneEvent( final ChangeSetEntity ret )
    {
        if ( null != datastoreCallback )
        {
            datastoreCallback.entityOperationDone( ret );
        }
    }
}
