package org.ctoolkit.migration.agent.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding model change descriptions.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSetModel
{
    /**
     * Optional Kind operations descriptors
     */
    @XmlElement( name = "kindOp" )
    private List<ChangeSetModelKindOp> kindOps;

    /**
     * Optional Kind property operations descriptors
     */
    @XmlElement( name = "kindPropsOp" )
    private List<ChangeSetModelKindPropOp> kindPropOps;

    public List<ChangeSetModelKindOp> getKindOps()
    {
        if ( kindOps == null )
        {
            kindOps = new ArrayList<>();
        }
        return kindOps;
    }

    public void setKindOps( List<ChangeSetModelKindOp> kindOps )
    {
        this.kindOps = kindOps;
    }

    public boolean hasKindOps()
    {
        return !getKindOps().isEmpty();
    }

    public List<ChangeSetModelKindPropOp> getKindPropOps()
    {
        if ( kindPropOps == null )
        {
            kindPropOps = new ArrayList<>();
        }
        return kindPropOps;
    }

    public void setKindPropOps( List<ChangeSetModelKindPropOp> kindPropOps )
    {
        this.kindPropOps = kindPropOps;
    }

    public boolean hasKindPropOps()
    {
        return !getKindPropOps().isEmpty();
    }

    @Override
    public String toString()
    {
        return "ChangeSetModel{" +
                "kindOps=" + kindOps +
                ", kindPropOps=" + kindPropOps +
                '}';
    }
}
