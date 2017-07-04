package org.ctoolkit.agent.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding migration set kind operations
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class MigrationSetKindOperations
{
    @XmlElement( name = "add" )
    private List<MigrationSetKindOperationAdd> add;

    @XmlElement( name = "change" )
    private List<MigrationSetKindOperationChange> change;

    @XmlElement( name = "remove" )
    private List<MigrationSetKindOperationRemove> remove;

    public List<MigrationSetKindOperationAdd> getAdd()
    {
        if ( add == null )
        {
            add = new ArrayList<>();
        }
        return add;
    }

    public void setAdd( List<MigrationSetKindOperationAdd> add )
    {
        this.add = add;
    }

    public List<MigrationSetKindOperationChange> getChange()
    {
        if ( change == null )
        {
            change = new ArrayList<>();
        }
        return change;
    }

    public void setChange( List<MigrationSetKindOperationChange> change )
    {
        this.change = change;
    }

    public List<MigrationSetKindOperationRemove> getRemove()
    {
        if ( remove == null )
        {
            remove = new ArrayList<>();
        }
        return remove;
    }

    public void setRemove( List<MigrationSetKindOperationRemove> remove )
    {
        this.remove = remove;
    }

    @Override
    public String toString()
    {
        return "MigrationSetKindOperations{" +
                "add=" + add +
                ", change=" + change +
                ", remove=" + remove +
                '}';
    }
}
