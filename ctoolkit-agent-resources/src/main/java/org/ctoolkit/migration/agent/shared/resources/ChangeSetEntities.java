package org.ctoolkit.migration.agent.shared.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding list of entity change descriptors.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSetEntities
        implements Serializable
{
    @XmlElement( name = "entity" )
    private List<ChangeSetEntity> entity;

    public List<ChangeSetEntity> getEntity()
    {
        if ( entity == null )
        {
            entity = new ArrayList<>();
        }
        return entity;
    }

    public void setEntity( List<ChangeSetEntity> entity )
    {
        this.entity = entity;
    }
}
