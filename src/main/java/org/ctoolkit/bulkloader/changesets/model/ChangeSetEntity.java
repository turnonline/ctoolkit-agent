package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Bean holding the Entity change description
 *         <p/>
 *         Examples:
 *         <p/>
 *         1) entity with only kind specified
 *         <p/>
 *         <entity kind="Member">
 *         <property name="prop1" type="type1" value="value1"/>
 *         </entity>
 *         <p/>
 *         <p/>
 *         2) entity with specified key - used for entity update
 *         <p/>
 *         <entity kind="Member" key="ABdghjahBDKABDkaDA78DASJHDKA">
 *         <property name="prop1" type="type1" value="value2"/>
 *         </entity>
 *         <p/>
 *         3) entity with id
 *         <p/>
 *         <entity kind="Member" id="151">
 *         <property name="prop1" type="type1" value="value2"/>
 *         </entity>
 *         <p/>
 *         4) entity with name
 *         <p/>
 *         <entity kind="Member" name="name">
 *         <property name="prop1" type="type1" value="value2"/>
 *         </entity>
 *         <p/>
 *         TODO: add sample for entity->parent relations
 */
@Root( name = "entity" )
public class ChangeSetEntity
{

    /**
     * Possible operations defined on Kind properties
     */
    public final static String OP_REMOVE = "remove";

    public final static String OP_CREATE = "";

    /**
     * Optional entity identifier
     */
    @Attribute( required = false )
    private String key;

    /**
     * Entity kind
     */
    @Attribute
    private String kind;

    /**
     * Optional entity identifier
     */
    @Attribute( required = false )
    private Long id;

    /**
     * Optional entity key name
     */
    @Attribute( required = false )
    private String name;

    /**
     * Optional parent identifier
     */
    @Attribute( required = false )
    private String parentKey;

    /**
     * Optional parent kind identifier
     */
    @Attribute( required = false )
    private String parentKind;

    /**
     * Optional parent id identifier
     */
    @Attribute( required = false )
    private Long parentId;

    /**
     * Optional parent entity key name
     */
    @Attribute( required = false )
    private String parentName;

    /**
     * Optional entity operation
     */
    @Attribute( required = false )
    private String op;

    /**
     * Optional entity properties
     */
    @ElementList( entry = "property", inline = true, required = false )
    private List<ChangeSetEntityProperty> properties;

    /**
     * Default constructor
     */
    public ChangeSetEntity()
    {
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public ChangeSetEntity( final ChangeSetEntity other, boolean copyProperties )
    {
        if ( null != other )
        {
            this.id = other.id;
            this.key = other.key;
            this.kind = other.kind;
            this.name = other.name;
            this.op = other.op;
            this.parentId = other.parentId;
            this.parentKind = other.parentKind;
            this.parentName = other.parentName;
            if ( copyProperties )
            {
                if ( null != other.properties )
                {
                    this.properties = new ArrayList<ChangeSetEntityProperty>( other.properties.size() );
                    for ( ChangeSetEntityProperty csp : other.properties )
                    {
                        this.properties.add( new ChangeSetEntityProperty( csp ) );
                    }
                }
            }
        }
    }

    /**
     * Constructor used by Test cases
     *
     * @param kind
     * @param key
     * @param op
     */
    public ChangeSetEntity( String kind, String key, String op )
    {
        this.kind = kind;
        this.key = key;
        this.op = op;
        this.properties = new ArrayList<ChangeSetEntityProperty>();
    }

    /**
     * Method creates an initialized change set entity
     *
     * @return initialized change set entity
     */
    public static ChangeSetEntity createChangeSetEntity()
    {
        ChangeSetEntity cse = new ChangeSetEntity();
        cse.setProperties( new ArrayList<ChangeSetEntityProperty>() );
        return cse;
    }

    /**
     * Adds a property to the property list
     *
     * @param property property to add
     */
    public void addProperty( ChangeSetEntityProperty property )
    {
        getProperties().add( property );
    }

    /**
     * @return the kind
     */
    public String getKind()
    {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind( String kind )
    {
        this.kind = kind;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey( String key )
    {
        this.key = key;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId( Long id )
    {
        this.id = id;
    }

    /**
     * @return the parentKind
     */
    public String getParentKind()
    {
        return parentKind;
    }

    /**
     * @param parentKind the parentKind to set
     */
    public void setParentKind( String parentKind )
    {
        this.parentKind = parentKind;
    }

    /**
     * @return the parentId
     */
    public Long getParentId()
    {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId( Long parentId )
    {
        this.parentId = parentId;
    }

    /**
     * @return the op
     */
    public String getOp()
    {
        return op;
    }

    /**
     * @param op the op to set
     */
    public void setOp( String op )
    {
        this.op = op;
    }

    /**
     * @return the keyName
     */
    public String getKeyName()
    {
        return name;
    }

    /**
     * @param keyName the keyName to set
     */
    public void setKeyName( String keyName )
    {
        this.name = keyName;
    }

    /**
     * @return the parentKeyName
     */
    public String getParentKeyName()
    {
        return parentName;
    }

    /**
     * @param parentKeyName the parentKeyName to set
     */
    public void setParentKeyName( String parentKeyName )
    {
        this.parentName = parentKeyName;
    }

    /**
     * @return the properties
     */
    public List<ChangeSetEntityProperty> getProperties()
    {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties( List<ChangeSetEntityProperty> properties )
    {
        this.properties = properties;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the parentName
     */
    public String getParentName()
    {
        return parentName;
    }

    /**
     * @param parentName the parentName to set
     */
    public void setParentName( String parentName )
    {
        this.parentName = parentName;
    }

    /**
     * @return the parentKey
     */
    public String getParentKey()
    {
        return parentKey;
    }

    /**
     * @param parentKey the parentKey to set
     */
    public void setParentKey( String parentKey )
    {
        this.parentKey = parentKey;
    }

    /**
     * @return true if the entity has attached properties
     */
    public boolean hasProperties()
    {
        return !getProperties().isEmpty();
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        String s;
        String id = "";
        if ( null != key )
        {
            id = key;
        }
        else if ( null != this.id )
        {
            id = this.id.toString();
        }
        else if ( null != this.name )
        {
            id = this.name;
        }
        String props = "{}";
        if ( null != getProperties() )
        {
            props = getProperties().toString();
        }
        if ( null != parentKind && null != parentId )
        {
            s = kind + "(" + id + ") <" + parentKind + "(" + parentId + ")>";
        }
        else
        {
            s = kind + "(" + id + ")";
        }
        return s + " " + props;
    }
}
