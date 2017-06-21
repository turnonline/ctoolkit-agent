/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.resource.ChangeSetEntity;

import java.util.List;

/**
 * The datastore interface as an abstraction over potential many underlying datastores.
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
@Deprecated
public interface DataAccess
{
    /**
     * Adds an entity described by change set to the data store.
     *
     * @param entity the entity to be added
     */
    @Deprecated
    void addEntity( ChangeSetEntity entity );

    /**
     * Export {@link ChangeSet} for specified entity name
     *
     * @param entityName name of entity
     * @return {@link ChangeSet}
     */
    @Deprecated
    ChangeSet exportChangeSet( String entityName );

    /**
     * Removes all entries from the entity of given kind.
     *
     * @param kind the entity kind to be cleared
     */
    @Deprecated
    void clearEntity( String kind );

    /**
     * Removes the entity kind from the data store.
     *
     * @param kind the entity kind to be removed
     */
    @Deprecated
    void dropEntity( String kind );

    /**
     * Adds a property with given type to the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be added
     * @param newType  the type of the property
     * @param newVal   the value of the property
     */
    @Deprecated
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
    @Deprecated
    void changeEntityProperty( String kind, String property, String newName, String newType, String newVal );

    /**
     * Removes a property from the entity.
     *
     * @param kind     the entity kind to be modified
     * @param property the name of the property to be removed
     */
    @Deprecated
    void removeEntityProperty( String kind, String property );

    /**
     * Get entity by key
     *
     * @param type class type
     * @param key entity key
     * @param <T>  entity type
     * @return entity
     */
    @Deprecated
    <T> T find( Class<T> type, com.google.cloud.datastore.Key key);

    /**
     * Get entities by specified filter
     *
     * @param filter filter for list
     * @param <T>    entity type
     * @return entity
     */
    @Deprecated
    <T extends BaseMetadata> List<T> find( BaseMetadataFilter<T> filter );

    /**
     * Get entities by specified filter
     *
     * @param filter filter for list
     * @return entity
     */
    @Deprecated
    List<MetadataAudit> find( AuditFilter filter );

    /**
     * Flush pool
     */
    @Deprecated
    void flushPool();
}
