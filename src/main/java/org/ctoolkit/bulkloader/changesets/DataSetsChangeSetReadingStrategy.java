package org.ctoolkit.bulkloader.changesets;

import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         DataSets reading strategy
 */
public class DataSetsChangeSetReadingStrategy
        implements ChangeSetReadingStrategy
{
    // TODO: move file name template to configuration bean
    private final static String CHANGESETS_FILE_TEMPLATE = "/dataset/changeset_%05d.xml";

    private Long version;

    private ChangeSet changeSet;

    private Logger logger = LoggerFactory.getLogger( DataSetsChangeSetReadingStrategy.class );

    @Inject
    public DataSetsChangeSetReadingStrategy( @Assisted Long fromVersion )
    {
        this.version = fromVersion;
    }

    @Override
    public boolean hasNext()
    {
        String filename = String.format( CHANGESETS_FILE_TEMPLATE, version + 1 );

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
    public String getCursor()
    {
        return "";
    }
}
