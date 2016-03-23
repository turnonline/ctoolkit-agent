package org.ctoolkit.agent.service;

import org.ctoolkit.agent.model.ChangeSet;
import org.ctoolkit.agent.model.ImportMetadata;
import org.ctoolkit.agent.model.JobInfo;

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
     * Update existing {@link ImportMetadata}
     *
     * @param importMetadata {@link ImportMetadata}
     * @return updated {@link ImportMetadata}
     */
    ImportMetadata updateImportMetadata( ImportMetadata importMetadata );

    /**
     * Get {@link ImportMetadata} for specified key
     *
     * @param key key of {@link ImportMetadata}
     * @return {@link ImportMetadata}
     */
    ImportMetadata getImportMetadata( String key );

    /**
     * Delete {@link ImportMetadata} for specified key
     *
     * @param key key of {@link ImportMetadata}
     */
    void deleteImportMetadata( String key );

    /**
     * Start map reduce job for specified {@link org.ctoolkit.agent.model.ImportMetadata}
     *
     * @param key key of {@link org.ctoolkit.agent.model.ImportMetadata}
     */
    void startImportJob( String key );

    /**
     * Cancel map reduce job for specified {@link org.ctoolkit.agent.model.ImportMetadata}
     *
     * @param key key of {@link org.ctoolkit.agent.model.ImportMetadata}
     */
    void cancelImportJob( String key );

    /**
     * Delete map reduce job for specified {@link org.ctoolkit.agent.model.ImportMetadata}
     *
     * @param key key of {@link org.ctoolkit.agent.model.ImportMetadata}
     */
    void deleteImportJob( String key );

    /**
     * Return map reduce job info
     *
     * @param key key of {@link org.ctoolkit.agent.model.ImportMetadata}
     * @return {@link JobInfo}
     */
    JobInfo getJobInfo( String key );

    /**
     * Process import data change set
     *
     * @param changeSet {@link ChangeSet} to process
     */
    void importChangeSet( final ChangeSet changeSet );
}
