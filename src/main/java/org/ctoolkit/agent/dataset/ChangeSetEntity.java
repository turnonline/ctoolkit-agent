package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding the Entity change description.
 * Examples:
 * <p/>
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
@Root( name = "entity" )
public class ChangeSetEntity
{
    /**
     * Possible operations defined on Kind properties
     */
    public final static String OP_REMOVE = "remove";

    @Attribute( required = false )
    private String key;

    @Attribute
    private String kind;

    @Attribute( required = false )
    private Long id;

    @Attribute( required = false )
    private String name;

    @Attribute( required = false )
    private String parentKey;

    @Attribute( required = false )
    private String parentKind;

    @Attribute( required = false )
    private Long parentId;

    @Attribute( required = false )
    private String parentName;

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
                    this.properties = new ArrayList<>( other.properties.size() );
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
     * @param kind the entity kind
     * @param key  the entity key
     * @param op   the entity operation
     */
    public ChangeSetEntity( String kind, String key, String op )
    {
        this.kind = kind;
        this.key = key;
        this.op = op;
        this.properties = new ArrayList<>();
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
     * Returns the entity kind.
     *
     * @return the entity kind
     */
    public String getKind()
    {
        return kind;
    }

    /**
     * Sets the entity kind.
     *
     * @param kind the entity kind to be set
     */
    public void setKind( String kind )
    {
        this.kind = kind;
    }

    /**
     * Returns the entity identifier, optional.
     *
     * @return the entity key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Sets the optional entity identifier.
     *
     * @param key the key to be set
     */
    public void setKey( String key )
    {
        this.key = key;
    }

    /**
     * Returns the entity identifier, optional.
     *
     * @return the entity id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the optional entity identifier.
     *
     * @param id the entity id to be set
     */
    public void setId( Long id )
    {
        this.id = id;
    }

    /**
     * Returns the parent kind, optional.
     *
     * @return the parent kind
     */
    public String getParentKind()
    {
        return parentKind;
    }

    /**
     * Sets optional parent kind.
     *
     * @param parentKind the parent kind to be set
     */
    public void setParentKind( String parentKind )
    {
        this.parentKind = parentKind;
    }

    /**
     * Returns the parent id identifier, optional.
     *
     * @return the parent Id
     */
    public Long getParentId()
    {
        return parentId;
    }

    /**
     * Sets optional parent id identifier.
     *
     * @param parentId the parent Id to be set
     */
    public void setParentId( Long parentId )
    {
        this.parentId = parentId;
    }

    /**
     * Returns the entity operation, optional.
     *
     * @return the entity operation
     */
    public String getOperation()
    {
        return op;
    }

    /**
     * Sets optional entity operation
     *
     * @param op the operation to be set
     */
    public void setOperation( String op )
    {
        this.op = op;
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
     * Returns the entity name, optional.
     *
     * @return the entity name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the optional entity name.
     *
     * @param name the entity name to be set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Returns the parent entity name, optional.
     *
     * @return the parent entity name
     */
    public String getParentName()
    {
        return parentName;
    }

    /**
     * Sets optional parent entity name.
     *
     * @param parentName the parentKeyName to set
     */
    public void setParentName( String parentName )
    {
        this.parentName = parentName;
    }

    /**
     * Returns the parent key identifier, optional.
     *
     * @return the parent key
     */
    public String getParentKey()
    {
        return parentKey;
    }

    /**
     * Sets optional parent key identifier.
     *
     * @param parentKey the parent key to be set
     */
    public void setParentKey( String parentKey )
    {
        this.parentKey = parentKey;
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
