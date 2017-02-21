package org.ctoolkit.migration.agent.shared.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding model change descriptions.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSetModel
        implements Serializable
{
    /**
     * Optional Kind operations descriptors
     */
    @XmlElement( name = "kindOp" )
    private List<ChangeSetModelKindOp> kindOp;

    /**
     * Optional Kind property operations descriptors
     */
    @XmlElement( name = "kindPropsOp" )
    private List<ChangeSetModelKindPropOp> kindPropsOp;

    public List<ChangeSetModelKindOp> getKindOp()
    {
        if ( kindOp == null )
        {
            kindOp = new ArrayList<>();
        }
        return kindOp;
    }

    public void setKindOp( List<ChangeSetModelKindOp> kindOp )
    {
        this.kindOp = kindOp;
    }

    public boolean hasKindOpsObject()
    {
        return !getKindOp().isEmpty();
    }

    public List<ChangeSetModelKindPropOp> getKindPropsOp()
    {
        if ( kindPropsOp == null )
        {
            kindPropsOp = new ArrayList<>();
        }
        return kindPropsOp;
    }

    public void setKindPropsOp( List<ChangeSetModelKindPropOp> kindPropsOp )
    {
        this.kindPropsOp = kindPropsOp;
    }

    public boolean hasKindPropOpsObject()
    {
        return !getKindPropsOp().isEmpty();
    }

    @Override
    public String toString()
    {
        return "ChangeSetModel{" +
                "kindOp=" + kindOp +
                ", kindPropsOp=" + kindPropsOp +
                '}';
    }
}
