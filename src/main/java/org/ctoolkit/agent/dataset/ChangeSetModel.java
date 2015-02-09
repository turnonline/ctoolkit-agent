package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * The bean holding model change descriptions.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Root( name = "model" )
public class ChangeSetModel
{
    /**
     * Optional Kind operations descriptors
     */
    @ElementList( name = "kindOp", inline = true, required = false )
    private List<KindOp> kindOps;

    /**
     * Optional kind property change descriptors
     */
    @ElementList( name = "kindOpProp", inline = true, required = false )
    private List<KindPropOp> kindPropOps;

    /**
     * Default constructor
     */
    public ChangeSetModel()
    {
    }

    /**
     * Returns the list of entity operations.
     *
     * @return the list of entity operations
     */
    public List<KindOp> getKindOps()
    {
        return kindOps;
    }

    /**
     * Sets the list of entity operations.
     *
     * @param kindOps the list of entity operations to be executed
     */
    public void setKindOps( List<KindOp> kindOps )
    {
        this.kindOps = kindOps;
    }

    /**
     * Returns the list of entity property operations.
     *
     * @return the list of entity property operations
     */
    public List<KindPropOp> getKindPropOps()
    {
        return kindPropOps;
    }

    /**
     * Sets the list of entity property operations.
     *
     * @param kindPropOps the list of entity property operations to be executed
     */
    public void setKindPropOps( List<KindPropOp> kindPropOps )
    {
        this.kindPropOps = kindPropOps;
    }

    /**
     * Returns true if this change set model has defined any entity operations.
     *
     * @return true if this change set model has defined any entity operations
     */
    public boolean hasKindOps()
    {
        return ( null != kindOps ) && ( !kindOps.isEmpty() );
    }

    /**
     * Returns true if this change set model has defined any entity property operations.
     *
     * @return true if this change set model has defined any entity property operations
     */
    public boolean hasKindPropOps()
    {
        return ( null != kindPropOps ) && ( !kindPropOps.isEmpty() );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "KindOps: " );
        for ( KindOp ko : kindOps )
        {
            sb.append( ko );
            sb.append( "\n" );
        }
        sb.append( "\nKindPropOps: " );
        for ( KindPropOp kpo : kindPropOps )
        {
            sb.append( kpo );
        }
        return sb.toString();
    }
}
