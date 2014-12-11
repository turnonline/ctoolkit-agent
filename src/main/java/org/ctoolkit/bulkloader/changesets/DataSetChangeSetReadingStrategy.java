package org.ctoolkit.bulkloader.changesets;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;
import org.ctoolkit.bulkloader.changesets.model.ChangeSets;
import org.simpleframework.xml.core.Persister;

import java.util.ListIterator;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         DataSet reading strategy
 */
public class DataSetChangeSetReadingStrategy
        implements ChangeSetReadingStrategy
{

    // TODO: move filename to configuration bean
    private final static String CHANGESETS_FILE = "/dataset/changesets.xml";

    private Long fromVersion;

    private ChangeSets changeSets;

    private ListIterator<ChangeSet> csIterator;

    public DataSetChangeSetReadingStrategy( Long fromVersion )
    {
        this.fromVersion = fromVersion;
        readChangeSets();
        resetChangeSetIterator();
    }

    private void readChangeSets()
    {
        try
        {
            changeSets = new Persister().read( ChangeSets.class, getClass().getResourceAsStream( CHANGESETS_FILE ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void resetChangeSetIterator()
    {
        if ( null != changeSets )
        {
            // set the iterator to the next changeset
            csIterator = changeSets.getChangeSets().listIterator();
            while ( csIterator.hasNext() )
            {
                ChangeSet cs = csIterator.next();
                if ( cs.getVersion().equals( fromVersion ) )
                {
                    break;
                }
                else if ( cs.getVersion() > fromVersion )
                {
                    // rollback iterator
                    csIterator.previous();
                    // iterator is set
                    break;
                }
            }
        }
    }

    @Override
    public boolean hasNext()
    {
        return csIterator.hasNext();
    }

    @Override
    public ChangeSet next()
    {
        return csIterator.next();
    }

    @Override
    public String getCursor()
    {
        return "";
    }
}
