package org.ctoolkit.migration.agent.service;

import org.ctoolkit.migration.agent.model.ChangeJobInfo;
import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;
import org.ctoolkit.migration.agent.model.ExportJobInfo;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.Filter;
import org.ctoolkit.migration.agent.model.ImportJobInfo;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.model.JobInfo;
import org.ctoolkit.migration.agent.model.KindMetaData;
import org.ctoolkit.migration.agent.model.PropertyMetaData;
import org.ctoolkit.migration.agent.shared.resources.ChangeSet;

import java.util.List;

/**
 * ChangeSet service API
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface ChangeSetService
{
    /**
     * Create new {@link ImportMetadata}
     *
     * @param importMetadata {@link ImportMetadata}
     * @return persisted {@link ImportMetadata}
     */
    ImportMetadata createImportMetadata( ImportMetadata importMetadata );

    /**
     * Create new {@link ImportMetadataItem}
     *
     * @param importMetadataItem {@link ImportMetadataItem}
     * @return persisted {@link ImportMetadataItem}
     */
    ImportMetadataItem createImportMetadataItem( ImportMetadataItem importMetadataItem );

    /**
     * Create new {@link ChangeMetadata}
     *
     * @param changeMetadata {@link ChangeMetadata}
     * @return persisted {@link ChangeMetadata}
     */
    ChangeMetadata createChangeMetadata( ChangeMetadata changeMetadata );

    /**
     * Create new {@link ChangeMetadataItem}
     *
     * @param changeMetadataItem {@link ChangeMetadataItem}
     * @return persisted {@link ChangeMetadataItem}
     */
    ChangeMetadataItem createChangeMetadataItem( ChangeMetadataItem changeMetadataItem );

    /**
     * Create new {@link ExportMetadata}
     *
     * @param exportMetadata {@link ExportMetadata}
     * @return persisted {@link ExportMetadata}
     */
    ExportMetadata createExportMetadata( ExportMetadata exportMetadata );

    /**
     * Update existing {@link ImportMetadata}
     *
     * @param importMetadata {@link ImportMetadata}
     * @return updated {@link ImportMetadata}
     */
    ImportMetadata updateImportMetadata( ImportMetadata importMetadata );

    /**
     * Update existing {@link ImportMetadataItem}
     *
     * @param importMetadataItem {@link ImportMetadataItem}
     * @return updated {@link ImportMetadataItem}
     */
    ImportMetadataItem updateImportMetadataItem( ImportMetadataItem importMetadataItem );

    /**
     * Update existing {@link ChangeMetadata}
     *
     * @param changeMetadata {@link ChangeMetadata}
     * @return updated {@link ChangeMetadata}
     */
    ChangeMetadata updateChangeMetadata( ChangeMetadata changeMetadata );

    /**
     * Update existing {@link ChangeMetadataItem}
     *
     * @param changeMetadataItem {@link ChangeMetadataItem}
     * @return updated {@link ChangeMetadataItem}
     */
    ChangeMetadataItem updateChangeMetadataItem( ChangeMetadataItem changeMetadataItem );

    /**
     * Update existing {@link ExportMetadata}
     *
     * @param exportMetadata {@link ExportMetadata}
     * @return updated {@link ExportMetadata}
     */
    ExportMetadata updateExportMetadata( ExportMetadata exportMetadata );

    /**
     * Get {@link ImportMetadata} for specified key
     *
     * @param key key of {@link ImportMetadata}
     * @return {@link ImportMetadata}
     */
    ImportMetadata getImportMetadata( String key );

    /**
     * Get {@link ImportMetadataItem} for specified key
     *
     * @param key key of {@link ImportMetadataItem}
     * @return {@link ImportMetadataItem}
     */
    ImportMetadataItem getImportMetadataItem( String key );

    /**
     * Get {@link ChangeMetadata} for specified key
     *
     * @param key key of {@link ChangeMetadata}
     * @return {@link ChangeMetadata}
     */
    ChangeMetadata getChangeMetadata( String key );

    /**
     * Get {@link ChangeMetadataItem} for specified key
     *
     * @param key key of {@link ChangeMetadataItem}
     * @return {@link ChangeMetadataItem}
     */
    ChangeMetadataItem getChangeMetadataItem( String key );

    /**
     * Get {@link ExportMetadata} for specified key
     *
     * @param key key of {@link ExportMetadata}
     * @return {@link ExportMetadata}
     */
    ExportMetadata getExportMetadata( String key );

    /**
     * Get list of {@link ImportMetadata}
     *
     * @param filter of {@link Filter}
     * @return list {@link ImportMetadata}
     */
    List<ImportMetadata> getImportMetadataList( Filter filter );

    /**
     * Get list of {@link ChangeMetadata}
     *
     * @param filter of {@link Filter}
     * @return list {@link ChangeMetadata}
     */
    List<ChangeMetadata> getChangeMetadataList( Filter filter );

    /**
     * Get list of {@link ExportMetadata}
     *
     * @param filter of {@link Filter}
     * @return list {@link ExportMetadata}
     */
    List<ExportMetadata> getExportMetadataList( Filter filter );

    /**
     * Delete {@link ImportMetadata} for specified key
     *
     * @param importMetadata {@link ImportMetadata}
     */
    void deleteImportMetadata( ImportMetadata importMetadata );

    /**
     * Delete {@link ImportMetadataItem} for specified key
     *
     * @param key key of {@link ImportMetadataItem}
     */
    void deleteImportMetadataItem( String key );

    /**
     * Delete {@link ChangeMetadata} for specified key
     *
     * @param changeMetadata {@link ChangeMetadata}
     */
    void deleteChangeMetadata( ChangeMetadata changeMetadata );

    /**
     * Delete {@link ChangeMetadataItem} for specified key
     *
     * @param key key of {@link ChangeMetadataItem}
     */
    void deleteChangeMetadataItem( String key );

    /**
     * Delete {@link ExportMetadata} for specified key
     *
     * @param exportMetadata {@link ExportMetadata}
     */
    void deleteExportMetadata( ExportMetadata exportMetadata );

    /**
     * Start map reduce job for specified {@link ImportMetadata}
     *
     * @param importMetadata {@link ImportMetadata}
     */
    void startImportJob( ImportMetadata importMetadata );

    /**
     * Start map reduce job for specified {@link ChangeMetadata}
     *
     * @param changeMetadata {@link ChangeMetadata}
     */
    void startChangeJob( ChangeMetadata changeMetadata );

    /**
     * Start map reduce job for specified {@link ExportMetadata}
     *
     * @param exportMetadata k{@link ExportMetadata}
     */
    void startExportJob( ExportMetadata exportMetadata );

    /**
     * Cancel map reduce job for specified {@link ImportMetadata}
     *
     * @param importMetadata {@link ImportMetadata}
     */
    void cancelImportJob( ImportMetadata importMetadata );

    /**
     * Cancel map reduce job for specified {@link ChangeMetadata}
     *
     * @param changeMetadata {@link ChangeMetadata}
     */
    void cancelChangeJob( ChangeMetadata changeMetadata );

    /**
     * Cancel map reduce job for specified {@link ExportMetadata}
     *
     * @param exportMetadata {@link ExportMetadata}
     */
    void cancelExportJob( ExportMetadata exportMetadata );

    /**
     * Delete map reduce job for specified {@link ImportMetadata}
     *
     * @param importMetadata  {@link ImportMetadata}
     */
    void deleteImportJob( ImportMetadata importMetadata );

    /**
     * Delete map reduce job for specified {@link ChangeMetadata}
     *
     * @param changeMetadata key of {@link ChangeMetadata}
     */
    void deleteChangeJob( ChangeMetadata changeMetadata );

    /**
     * Delete map reduce job for specified {@link ExportMetadata}
     *
     * @param exportMetadata key of {@link ExportMetadata}
     */
    void deleteExportJob( ExportMetadata exportMetadata );

    /**
     * Return map reduce job info
     *
     * @param importMetadata {@link ImportMetadata}
     * @return {@link JobInfo}
     */
    ImportJobInfo getImportJobInfo( ImportMetadata importMetadata );

    /**
     * Return map reduce job info
     *
     * @param changeMetadata {@link ChangeMetadata}
     * @return {@link JobInfo}
     */
    ChangeJobInfo getChangeJobInfo( ChangeMetadata changeMetadata );

    /**
     * Return map reduce job info
     *
     * @param exportMetadata {@link ExportMetadata}
     * @return {@link JobInfo}
     */
    ExportJobInfo getExportJobInfo( ExportMetadata exportMetadata );

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
