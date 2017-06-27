package org.ctoolkit.agent.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Migration set descriptors.
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@XmlRootElement( name = "migrationset" )
@XmlAccessorType( XmlAccessType.FIELD )
public class MigrationSet
        implements Serializable
{
    @XmlAttribute( name = "author" )
    private String author;

    @XmlAttribute( name = "comment" )
    private String comment;

    @XmlElement( name = "operation" )
    private List<MigrationSetKindOp> kindOps;

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

    public List<MigrationSetKindOp> getKindOps()
    {
        if ( kindOps == null )
        {
            kindOps = new ArrayList<>();
        }
        return kindOps;
    }

    public void setKindOps( List<MigrationSetKindOp> kindOps )
    {
        this.kindOps = kindOps;
    }

    @Override
    public String toString()
    {
        return "MigrationSet{" +
                "author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", kindOps=" + kindOps +
                '}';
    }
}
