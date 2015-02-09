package org.ctoolkit.agent.dataset.reader.impl;

import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.agent.dataset.ChangeSet;
import org.ctoolkit.agent.dataset.reader.DataSetReadingStrategy;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * The reading strategy that's reading multiple data set xml files that reside on local file system in sequence.
 * The sequence is defined by file name pattern.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DataSetMultipleXmlReader
        implements DataSetReadingStrategy
{
    public static final String FILE_NAME_PATTERN = "/dataset/changeset_%05d.xml";

    private final String fileNamePattern;

    private Long version;

    private ChangeSet changeSet;

    private Logger logger = LoggerFactory.getLogger( DataSetMultipleXmlReader.class );

    @Inject
    public DataSetMultipleXmlReader( @Assisted Long fromVersion, @Assisted String fileNamePattern )
    {
        this.version = fromVersion;
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public boolean hasNext()
    {
        String filename = String.format( fileNamePattern, version + 1 );

        try
        {
            InputStream dataSourceStream = getClass().getResourceAsStream( filename );
            if ( dataSourceStream != null )
            {
                changeSet = new Persister().read( ChangeSet.class, dataSourceStream );
                version++;
                changeSet.setVersion( version );
            }
            else
            {
                changeSet = null;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            changeSet = null;
            logger.error( "File " + filename + " Version: " + version, e );
        }
        return null != changeSet;
    }

    @Override
    public ChangeSet next()
    {
        return changeSet;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCursor()
    {
        return "";
    }
}
