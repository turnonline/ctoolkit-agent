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

import com.google.cloud.datastore.Entity;
import org.ctoolkit.agent.exception.MigrationUseCaseNotExists;
import org.ctoolkit.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.resource.ChangeSet;
import org.ctoolkit.agent.resource.MigrationSet;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.service.impl.event.AuditEvent;

import java.util.List;

/**
 * ChangeSet service API
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface ChangeSetService
{
    // ------------------------------------------
    // -- metadata
    // ------------------------------------------

    /**
     * Create new {@link BaseMetadata}
     *
     * @param metadata {@link BaseMetadata}
     * @return persisted {@link BaseMetadata}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M create( M metadata );

    /**
     * Update existing {@link BaseMetadata}
     *
     * @param metadata {@link BaseMetadata}
     * @return persisted {@link BaseMetadata}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> M update( M metadata );

    /**
     * Get {@link BaseMetadata} for specified key
     *
     * @param key contains key of {@link BaseMetadata}
     * @return persisted {@link BaseMetadata}
     */
    <M extends BaseMetadata> M get( MetadataKey<M> key );

    /**
     * Delete {@link BaseMetadata}
     *
     * @param metadata {@link BaseMetadata}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( M metadata );

    /**
     * Get list of {@link BaseMetadata}
     *
     * @param filter of {@link BaseMetadataFilter}
     * @return list {@link BaseMetadata}
     */
    <M extends BaseMetadata> List<M> list( BaseMetadataFilter<M> filter );

    // ------------------------------------------
    // -- metadata item
    // ------------------------------------------

    /**
     * Create new {@link BaseMetadataItem}
     *
     * @param metadata     {@link BaseMetadata}
     * @param metadataItem {@link BaseMetadataItem}
     * @return persisted {@link BaseMetadataItem}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI create( M metadata, MI metadataItem );

    /**
     * Update existing {@link BaseMetadataItem}
     *
     * @param metadataItem {@link BaseMetadataItem}
     * @return persisted {@link BaseMetadataItem}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI update( MI metadataItem );

    /**
     * Get {@link BaseMetadata} for specified key
     *
     * @param key contains key of {@link BaseMetadata}
     * @return persisted {@link BaseMetadata}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI get( MetadataItemKey<M, MI> key );

    /**
     * Delete {@link BaseMetadataItem}
     *
     * @param metadataItem {@link BaseMetadataItem}
     */
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> void delete( MI metadataItem );

    // ------------------------------------------
    // -- job
    // ------------------------------------------

    /**
     * Start job for specified {@link BaseMetadata}
     *
     * @param metadata {@link BaseMetadata}
     * @throws ProcessAlreadyRunning if job is already running
     */
    <M extends BaseMetadata> void startJob( M metadata ) throws ProcessAlreadyRunning;

    /**
     * Return {@link JobInfo} for specified metadata
     *
     * @param metadata {@link BaseMetadata}
     * @return {@link JobInfo}
     */
    <JI extends JobInfo, M extends BaseMetadata> JI getJobInfo( M metadata );

    /**
     * Cancel map reduce job for specified {@link BaseMetadata}
     *
     * @param metadata {@link BaseMetadata}
     */
    <M extends BaseMetadata> void cancelJob( M metadata );

    // ------------------------------------------
    // -- job core BL
    // ------------------------------------------

    /**
     * Process import data change set
     *
     * @param changeSet {@link ChangeSet} to process
     */
    void importChangeSet( ChangeSet changeSet );

    /**
     * Process change data change set
     *
     * @param entity entity object to export
     * @return change set containing change set data
     */
    ChangeSet exportChangeSet( String entity );

    /**
     * Migrate data with specified operation on entity
     *
     * @param operation {@link MigrationSet}
     * @param entity    {@link Entity}
     * @throws MigrationUseCaseNotExists if no use case is found for {@link MigrationSetKindOperation}
     */
    void migrate( MigrationSetKindOperation operation, Entity entity ) throws MigrationUseCaseNotExists;

    // ------------------------------------------
    // -- audits
    // ------------------------------------------

    /**
     * Create metadata audit
     *
     * @param event {@link AuditEvent}
     */
    void create( AuditEvent event );

    /**
     * Filter {@link MetadataAudit} by specified filter
     *
     * @param filter {@link AuditFilter}
     * @return list of {@link MetadataAudit}
     */
    List<MetadataAudit> list( AuditFilter filter );

    // ------------------------------------------
    // -- meta infos
    // ------------------------------------------

    /**
     * Return list of {@link KindMetaData}
     *
     * @return {@link KindMetaData}
     */
    List<KindMetaData> kinds();

    /**
     * Return list of {@link PropertyMetaData}
     *
     * @param kind entity kind
     * @return list of {@link PropertyMetaData}
     */
    List<PropertyMetaData> properties( String kind );

    void flushPool();
}
