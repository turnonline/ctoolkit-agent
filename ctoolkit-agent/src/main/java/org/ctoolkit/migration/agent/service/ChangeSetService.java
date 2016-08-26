package org.ctoolkit.migration.agent.service;

import org.ctoolkit.migration.agent.model.ChangeMetadata;
import org.ctoolkit.migration.agent.model.ChangeMetadataItem;
import org.ctoolkit.migration.agent.model.ExportMetadata;
import org.ctoolkit.migration.agent.model.Filter;
import org.ctoolkit.migration.agent.model.ImportMetadata;
import org.ctoolkit.migration.agent.model.ImportMetadataItem;
import org.ctoolkit.migration.agent.model.JobInfo;
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
     * @param key key of {@link ImportMetadata}
     */
    void deleteImportMetadata( String key );

    /**
     * Delete {@link ImportMetadataItem} for specified key
     *
     * @param key key of {@link ImportMetadataItem}
     */
    void deleteImportMetadataItem( String key );

    /**
     * Delete {@link ChangeMetadata} for specified key
     *
     * @param key key of {@link ChangeMetadata}
     */
    void deleteChangeMetadata( String key );

    /**
     * Delete {@link ChangeMetadataItem} for specified key
     *
     * @param key key of {@link ChangeMetadataItem}
     */
    void deleteChangeMetadataItem( String key );

    /**
     * Delete {@link ExportMetadata} for specified key
     *
     * @param key key of {@link ExportMetadata}
     */
    void deleteExportMetadata( String key );

    /**
     * Start map reduce job for specified {@link ImportMetadata}
     *
     * @param key key of {@link ImportMetadata}
     */
    void startImportJob( String key );

    /**
     * Start map reduce job for specified {@link ChangeMetadata}
     *
     * @param key key of {@link ChangeMetadata}
     */
    void startChangeJob( String key );

    /**
     * Start map reduce job for specified {@link ExportMetadata}
     *
     * @param key key of {@link ExportMetadata}
     */
    void startExportJob( String key );

    /**
     * Cancel map reduce job for specified {@link ImportMetadata}
     *
     * @param key key of {@link ImportMetadata}
     */
    void cancelImportJob( String key );

    /**
     * Cancel map reduce job for specified {@link ChangeMetadata}
     *
     * @param key key of {@link ChangeMetadata}
     */
    void cancelChangeJob( String key );

    /**
     * Cancel map reduce job for specified {@link ExportMetadata}
     *
     * @param key key of {@link ExportMetadata}
     */
    void cancelExportJob( String key );

    /**
     * Delete map reduce job for specified {@link ImportMetadata}
     *
     * @param key key of {@link ImportMetadata}
     */
    void deleteImportJob( String key );

    /**
     * Delete map reduce job for specified {@link ChangeMetadata}
     *
     * @param key key of {@link ChangeMetadata}
     */
    void deleteChangeJob( String key );

    /**
     * Delete map reduce job for specified {@link ExportMetadata}
     *
     * @param key key of {@link ExportMetadata}
     */
    void deleteExportJob( String key );

    /**
     * Return map reduce job info
     *
     * @param key key of {@link ImportMetadata}
     * @return {@link JobInfo}
     */
    JobInfo getImportJobInfo( String key );

    /**
     * Return map reduce job info
     *
     * @param key key of {@link ChangeMetadata}
     * @return {@link JobInfo}
     */
    JobInfo getChangeJobInfo( String key );

    /**
     * Return map reduce job info
     *
     * @param key key of {@link ExportMetadata}
     * @return {@link JobInfo}
     */
    JobInfo getExportJobInfo( String key );

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
     * @return byte array containing change set data
     */
    byte[] exportChangeSet( final String entity );
}
