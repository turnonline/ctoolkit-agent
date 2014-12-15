package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Bean holding list of entity change descriptors
 */
@Root( name = "entities" )
public class ChangeSetEntities
{

    /**
     * Optional entity list
     */
    @ElementList( inline = true )
    private List<ChangeSetEntity> entities;

    /**
     * Default constructor
     */
    public ChangeSetEntities()
    {
    }

    /**
     * @return the entities
     */
    public List<ChangeSetEntity> getEntities()
    {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities( List<ChangeSetEntity> entities )
    {
        this.entities = entities;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if ( entities.size() > 4 )
        {
            sb.append( entities.get( 0 ) );
            sb.append( "\n" );
            sb.append( entities.get( 1 ) );
            sb.append( "\n...\n" );
            sb.append( entities.get( entities.size() - 2 ) );
            sb.append( "\n" );
            sb.append( entities.get( entities.size() - 1 ) );
            sb.append( "\n" );
        }
        else
        {
            for ( ChangeSetEntity e : entities )
            {
                sb.append( e );
                sb.append( "\n" );
            }
        }
        return sb.toString();
    }
}
