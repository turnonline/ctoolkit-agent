package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * The bean holding list of entity change descriptors.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Root( name = "entities" )
public class ChangeSetEntities
{
    @ElementList( inline = true )
    private List<ChangeSetEntity> entities;

    /**
     * Default constructor
     */
    public ChangeSetEntities()
    {
    }

    /**
     * Returns the entity list.
     *
     * @return the entities
     */
    public List<ChangeSetEntity> getEntities()
    {
        return entities;
    }

    /**
     * Sets the entity list.
     *
     * @param entities the entities to be set
     */
    public void setEntities( List<ChangeSetEntity> entities )
    {
        this.entities = entities;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
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
