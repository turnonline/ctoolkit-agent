package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.ChangeSet;
import org.ctoolkit.agent.model.ChangeSetEntity;

/**
 * The datastore interface as an abstraction over potential many underlying datastores.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface DataAccess
{
    /**
     * Adds an entity described by change set to the data store.
     *
     * @param entity the entity to be added
     */
    void addEntity( ChangeSetEntity entity );

    /**
     * Export {@link ChangeSet} for specified entity name
     *
     * @param entityName name of entity
     * @return {@link ChangeSet}
     */
    ChangeSet exportChangeSet( String entityName );

    /**
     * Removes all entries from the entity of given kind.
     *
     * @param kind the entity kind to be cleared
     */
    void clearEntity( String kind );

    /**
     * Removes the entity kind from the data store.
     *
     * @param kind the entity kind to be removed
     */
    void dropEntity( String kind );

    /**
     * Adds a property with given type to the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be added
     * @param newType  the type of the property
     * @param newVal   the value of the property
     */
    void addEntityProperty( String kind, String property, String newType, String newVal );

    /**
     * Change a property with given attributes.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property which will be changed
     * @param newName  the name of the property to be changed
     * @param newType  the type of the property to be changed
     * @param newVal   the value of the property to be changed
     */
    void changeEntityProperty( String kind, String property, String newName, String newType, String newVal );

    /**
     * Removes a property from the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be removed
     */
    void removeEntityProperty( String kind, String property );

    /**
     * Create entity
     *
     * @param entity entity to create
     * @param <T>    entity type
     * @return persisted entity
     */
    <T> T create( T entity );

    /**
     * Update entity
     *
     * @param entity entity to update
     * @param <T>    entity type
     * @return updated entity
     */
    <T> T update( T entity );

    /**
     * Get entity by key
     *
     * @param type entity class type
     * @param key  key of entity
     * @param <T>  entity type
     * @return entity
     */
    <T> T find( Class<T> type, String key );

    /**
     * Delete entity
     *
     * @param type entity class type
     * @param key  key of entity
     * @param <T>  entity type
     */
    <T> void delete( Class<T> type, String key );
}
