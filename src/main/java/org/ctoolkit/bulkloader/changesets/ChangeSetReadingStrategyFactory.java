package org.ctoolkit.bulkloader.changesets;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ChangeSetReadingStrategyFactory
{
    @Named( "DataSets" )
    ChangeSetReadingStrategy createDataSets( Long fromVersion );

    @Named( "DataStore" )
    ChangeSetReadingStrategy createDataStore( @Assisted( "job" ) String job,
                                              @Assisted( "kind" ) String kind,
                                              @Assisted( "cursor" ) String cursor );

    @Named( "ExportStore" )
    ChangeSetReadingStrategy createExportStore( @Assisted( "maxVersion" ) Long maxVersion,
                                                @Assisted( "version" ) Long version,
                                                @Assisted( "subVersion" ) Long subVersion,
                                                String cursor );
}
