package org.ctoolkit.bulkloader.changesets;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.exportstore.ExportStore;

import javax.inject.Inject;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ExportStoreChangeSetReadingStrategy
        implements ChangeSetReadingStrategy
{

    private final ExportStore exportStore;

    private boolean nextChangeSetIsRead = false;

    private ChangeSet changeSet;

    /**
     * Sets the internal cursor to the 'cursor'
     *
     * @param exportStore
     * @param maxVersion
     * @param version
     * @param subVersion
     * @param cursor
     */
    @Inject
    public ExportStoreChangeSetReadingStrategy( ExportStore exportStore,
                                                @Assisted( "maxVersion" ) Long maxVersion,
                                                @Assisted( "version" ) Long version,
                                                @Assisted( "subVersion" ) Long subVersion,
                                                @Assisted String cursor )
    {
        this.exportStore = exportStore;

        if ( !StringUtil.isEmptyOrWhitespace( cursor ) )
        {
            exportStore.setFirstChangeSet( maxVersion, version, subVersion, cursor );
        }
        else
        {
            exportStore.setFirstChangeSet( maxVersion );
        }
    }

    @Override
    public boolean hasNext()
    {
        if ( !nextChangeSetIsRead )
        {
            changeSet = exportStore.getNextChangeSet();
            nextChangeSetIsRead = true;
        }
        return ( ( null != changeSet ) && ( !changeSet.isEmpty() ) );
    }

    @Override
    public ChangeSet next()
    {
        if ( !nextChangeSetIsRead )
        {
            changeSet = exportStore.getNextChangeSet();
        }
        nextChangeSetIsRead = false;
        return changeSet;
    }

    @Override
    public String getCursor()
    {
        return exportStore.getCursor();
    }
}
