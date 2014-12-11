package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Bean holding one entity property
 *         <p/>
 *         Example:
 *         <property name="prop1" type="type" {value="value"}/>
 */
@Root( name = "property" )
public class ChangeSetEntityProperty
{

    /**
     * Supported change set entity property types
     */
    public static final String PROPERTY_TYPE_STRING = "string";

    public static final String PROPERTY_TYPE_FLOAT = "float";

    public static final String PROPERTY_TYPE_DOUBLE = "double";

    public static final String PROPERTY_TYPE_INTEGER = "int";

    public static final String PROPERTY_TYPE_LONG = "long";

    public static final String PROPERTY_TYPE_DATE = "date";

    public static final String PROPERTY_TYPE_BOOLEAN = "boolean";

    public static final String PROPERTY_TYPE_SHORTBLOB = "shortblob";

    public static final String PROPERTY_TYPE_BLOB = "blob";

    public static final String PROPERTY_TYPE_NULL = "null";

    public static final String PROPERTY_TYPE_KEY = "key";

    public static final String PROPERTY_TYPE_KEY_NAME = "key-name";

    public static final String PROPERTY_TYPE_TEXT = "text";

    public static final String PROPERTY_TYPE_EXTERNAL_TEXT = "external-text";

    public static final String PROPERTY_TYPE_LIST_KEY = "list-key";

    public static final String PROPERTY_TYPE_LIST_LONG = "list-long";

    public static final String PROPERTY_TYPE_LIST_ENUM = "list-enum";

    public static final String PROPERTY_TYPE_LIST_STRING = "list-string";

    /**
     * Property name
     */
    @Attribute
    private String name;

    /**
     * Property type
     */
    @Attribute
    private String type;

    /**
     * Property value
     * If the value attribute is missing its value become null
     */
    @Attribute( required = false )
    private String value;

    /**
     * Default constructor
     */
    public ChangeSetEntityProperty()
    {
    }

    /**
     * Constructor
     *
     * @param name
     * @param type
     * @param value
     */
    public ChangeSetEntityProperty( String name, String type, String value )
    {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Copy constructor
     *
     * @param other property to copy
     */
    public ChangeSetEntityProperty( final ChangeSetEntityProperty other )
    {
        this( other.name, other.type, other.value );
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
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType( String type )
    {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        return type + " " + name + " = " + value;
    }
}
