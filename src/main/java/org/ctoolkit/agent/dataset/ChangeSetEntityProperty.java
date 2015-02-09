package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * The bean holding one entity property.
 * Example:
 * <pre>
 * {@code
 * <property name="prop1" type="type" {value="value"}/>
 * }
 * </pre>
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
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

    @Attribute
    private String name;

    @Attribute
    private String type;

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
     * @param name  the property name to be set
     * @param type  the property type to be set
     * @param value the property value to be set
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
     * Returns the property name.
     *
     * @return the property name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the property name.
     *
     * @param name the property name to be set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Returns the property type.
     *
     * @return the property type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the property type.
     *
     * @param type the property type to be set
     */
    public void setType( String type )
    {
        this.type = type;
    }

    /**
     * Returns the property value.
     *
     * @return the property value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the property value. If the value attribute is missing its value become <tt>null</tt>.
     *
     * @param value the property value to be set
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return type + " " + name + " = " + value;
    }
}
