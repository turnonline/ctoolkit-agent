package org.ctoolkit.agent.service;

import org.ctoolkit.agent.exception.ProcessAlreadyRunning;
import org.ctoolkit.agent.model.AuditFilter;
import org.ctoolkit.agent.model.BaseMetadata;
import org.ctoolkit.agent.model.BaseMetadataFilter;
import org.ctoolkit.agent.model.BaseMetadataItem;
import org.ctoolkit.agent.model.ExportMetadata;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;
import org.ctoolkit.agent.model.KindMetaData;
import org.ctoolkit.agent.model.MetadataAudit;
import org.ctoolkit.agent.model.MetadataItemKey;
import org.ctoolkit.agent.model.MetadataKey;
import org.ctoolkit.agent.model.PropertyMetaData;
import org.ctoolkit.agent.shared.resources.ChangeSet;

import java.io.IOException;
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

    /**
     * Migrate data from one agent to another
     *
     * @param exportMetadata {@link ExportMetadata} as source for {@link ImportMetadata}
     * @return {@link ImportMetadata}
     */
    ImportMetadata migrate( ExportMetadata exportMetadata ) throws IOException;

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
    <MI extends BaseMetadataItem<M>, M extends BaseMetadata<MI>> MI get( MetadataItemKey<MI> key );

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
     * Delete map reduce job for specified {@link BaseMetadata}
     *
     * @param metadata key of {@link BaseMetadata}
     */
    <M extends BaseMetadata> void deleteJob( M metadata );

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
    // -- changesets
    // ------------------------------------------

    /**
     * Process import data change set
     *
     * @param changeSet {@link ChangeSet} to process
     */
    void importChangeSet( final ChangeSet changeSet );

    /**
     * Process change data change set
     *
     * @param changeSet {@link ChangeSet} to process
     */
    void changeChangeSet( final ChangeSet changeSet );

    /**
     * Process change data change set
     *
     * @param entity entity object to export
     * @return change set containing change set data
     */
    ChangeSet exportChangeSet( final String entity );

    // ------------------------------------------
    // -- audits
    // ------------------------------------------

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
}