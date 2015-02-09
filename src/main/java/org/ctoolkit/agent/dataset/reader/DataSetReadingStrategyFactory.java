package org.ctoolkit.agent.dataset.reader;

import com.google.inject.name.Named;

/**
 * Data set reading strategy factory.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface DataSetReadingStrategyFactory
{
    @Named( "DataSets" )
    DataSetReadingStrategy createDataSetReader( Long fromVersion, String fileNamePattern );
}
