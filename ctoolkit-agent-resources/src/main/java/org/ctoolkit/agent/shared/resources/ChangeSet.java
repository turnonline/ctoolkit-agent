package org.ctoolkit.agent.shared.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Change set change descriptors.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlRootElement( name = "changeset" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSet
        implements Serializable
{
    @XmlAttribute( name = "author" )
    private String author;

    @XmlAttribute( name = "comment" )
    private String comment;

    @XmlElement( name = "model" )
    private ChangeSetModel model;

    @XmlElement( name = "entities" )
    private ChangeSetEntities entities;

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor( String author )
    {
        this.author = author;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public ChangeSetModel getModel()
    {
        return model;
    }

    public void setModel( ChangeSetModel model )
    {
        this.model = model;
    }

    public boolean hasModelObject()
    {
        return getModel() != null && ( getModel().hasKindOpsObject() || getModel().hasKindPropOpsObject() );
    }

    public ChangeSetEntities getEntities()
    {
        return entities;
    }

    public void setEntities( ChangeSetEntities entities )
    {
        this.entities = entities;
    }

    public boolean hasEntities()
    {
        return getEntities() != null && !getEntities().getEntity().isEmpty();
    }

    @Override
    public String toString()
    {
        return "ChangeSet{" +
                "author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", model=" + model +
                ", entities=" + entities +
                '}';
    }
}