package org.ctoolkit.bulkloader.changesets;

import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.conf.Configuration;
import org.ctoolkit.bulkloader.conf.model.ExportJob;
import org.ctoolkit.bulkloader.datastore.DataStore;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: TT
 * Date: 17.3.2011
 * Time: 22:44
 */
public class DataStoreChangeSetReadingStrategy
        implements ChangeSetReadingStrategy
{

    /**
     * Global configuration data
     */
    private final Configuration configuration;

    /**
     * Data store to work on
     */
    private final DataStore dataStore;

    private boolean nextChangeSetIsRead = false;

    private ChangeSet changeSet;

    /**
     * Flag,when it is set, the data store is initialized and is ready for reading the next changeset
     * The value is false, when the input job is invalid - there is no entry with this name in the
     * export job configuration.
     */
    private boolean initialized = false;

    /**
     * Constructor
     *
     * @param configuration
     * @param dataStore
     * @param job
     * @param kind
     * @param cursor
     */
    @Inject
    public DataStoreChangeSetReadingStrategy( Configuration configuration,
                                              DataStore dataStore,
                                              @Assisted( "job" ) String job,
                                              @Assisted( "kind" ) String kind,
                                              @Assisted( "cursor" ) String cursor )
    {
        this.configuration = configuration;
        this.dataStore = dataStore;

        // set the pointer to the next record
        if ( null == kind )
        {
            // look for the job

            // read the first kind form the configuration
            if ( null != configuration.getExportJobs().getJobs() && configuration.getExportJobs().getJobs().size() > 0 )
            {
                for ( ExportJob exportJob : configuration.getExportJobs().getJobs() )
                {
                    if ( job.equalsIgnoreCase( exportJob.getName() ) )
                    {
                        if ( null != exportJob.getKinds() && exportJob.getKinds().getKinds().size() > 0 )
                        {
                            kind = exportJob.getKinds().getKinds().get( 0 ).getName();
                            break;
                        }
                    }
                }
            }
        }

        if ( null == kind )
        {
            // nothing to do
            return;
        }

        // set the data store pointer
        dataStore.setFirstChangeSet( kind, cursor );
        initialized = true;
    }

    @Override
    public boolean hasNext()
    {
        if ( !initialized )
        {
            return false;
        }

        if ( !nextChangeSetIsRead )
        {
            changeSet = dataStore.getNextChangeSet();
            nextChangeSetIsRead = true;
        }
        return ( ( null != changeSet ) && ( !changeSet.isEmpty() ) );
    }

    @Override
    public ChangeSet next()
    {
        if ( !initialized )
        {
            return null;
        }
        if ( !nextChangeSetIsRead )
        {
            changeSet = dataStore.getNextChangeSet();
        }
        nextChangeSetIsRead = false;
        return changeSet;
    }

    @Override
    public String getCursor()
    {
        if ( !initialized )
        {
            return null;
        }
        return dataStore.getCursor();
    }
}
