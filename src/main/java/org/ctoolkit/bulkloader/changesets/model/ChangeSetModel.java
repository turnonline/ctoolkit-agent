package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Bean holding model change descriptions
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
     * @return the kindOps
     */
    public List<KindOp> getKindOps()
    {
        return kindOps;
    }

    /**
     * @param kindOps the kindOps to set
     */
    public void setKindOps( List<KindOp> kindOps )
    {
        this.kindOps = kindOps;
    }

    /**
     * @return the kindPropOps
     */
    public List<KindPropOp> getKindPropOps()
    {
        return kindPropOps;
    }

    /**
     * @param kindPropOps the kindPropOps to set
     */
    public void setKindPropOps( List<KindPropOp> kindPropOps )
    {
        this.kindPropOps = kindPropOps;
    }

    public boolean hasKindOps()
    {
        return ( null != kindOps ) && ( !kindOps.isEmpty() );
    }

    public boolean hasKindPropOps()
    {
        return ( null != kindPropOps ) && ( !kindPropOps.isEmpty() );
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
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
