package org.ctoolkit.agent.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding the Entity change description.
 * Examples:
 * 1) entity with only kind specified
 * <pre>
 * {@code
 * <entity kind="Member">
 *     <property name="prop1" type="type1" value="value1"/>
 * </entity>
 * }
 * </pre>
 * 2) entity with specified key - used for entity update
 * <pre>
 * {@code
 * <entity kind="Member" key="ABdghjahBDKABDkaDA78DASJHDKA">
 *     <property name="prop1" type="type1" value="value2"/>
 * </entity>
 * }
 * </pre>
 * 3) entity with id
 * <pre>
 * {@code
 * <entity kind="Member" id="151">
 *     <property name="prop1" type="type1" value="value2"/>
 * </entity>
 * }
 * </pre>
 * 4) entity with name
 * <pre>
 * {@code
 * <entity kind="Member" name="name">
 *     <property name="prop1" type="type1" value="value2"/>
 * </entity>
 * }
 * </pre>
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSetEntity
{
    @XmlAttribute(name = "key")
    private String key;

    @XmlAttribute(name = "kind")
    private String kind;

    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name =  "parentKey")
    private String parentKey;

    @XmlAttribute(name = "parentKind")
    private String parentKind;

    @XmlAttribute(name = "parentId")
    private Long parentId;

    @XmlAttribute(name = "parentName")
    private String parentName;

    /**
     * Optional entity properties
     */
    @XmlElement( name = "property" )
    private List<ChangeSetEntityProperty> properties;

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getParentKey()
    {
        return parentKey;
    }

    public void setParentKey( String parentKey )
    {
        this.parentKey = parentKey;
    }

    public String getParentKind()
    {
        return parentKind;
    }

    public void setParentKind( String parentKind )
    {
        this.parentKind = parentKind;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId( Long parentId )
    {
        this.parentId = parentId;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName( String parentName )
    {
        this.parentName = parentName;
    }

    public List<ChangeSetEntityProperty> getProperties()
    {
        if (properties == null) {
            properties = new ArrayList<>(  );
        }
        return properties;
    }

    public void setProperties( List<ChangeSetEntityProperty> properties )
    {
        this.properties = properties;
    }

    /**
     * Returns true if this change set entity has attached properties.
     *
     * @return true if this change set entity has attached properties
     */
    public boolean hasProperties()
    {
        return !getProperties().isEmpty();
    }

    @Override
    public String toString()
    {
        return "ChangeSetEntity{" +
                "key='" + key + '\'' +
                ", kind='" + kind + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parentKey='" + parentKey + '\'' +
                ", parentKind='" + parentKind + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
