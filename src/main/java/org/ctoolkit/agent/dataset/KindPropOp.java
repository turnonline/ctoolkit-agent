package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * The bean holding entity kind property model update description
 * Example:
 * <pre>
 * {@code
 * <kindprop op="update" kind="kind" property="prop" defval="" newname="" newtype="" migratevalue="" reason="blabla"/>
 * }
 * </pre>
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Root
public class KindPropOp
{
    /**
     * Possible operations defined on Kind properties
     */
    public final static String OP_ADD = "add";

    public final static String OP_REMOVE = "remove";

    public final static String OP_CHANGE = "change";

    public final static String OP_UPDATE = "update";

    @Attribute
    private String op;

    @Attribute
    private String kind;

    @Attribute
    private String property;

    @Attribute( required = false )
    private String type;

    @Attribute( required = false )
    private String defVal;

    @Attribute( required = false )
    private String newName;

    @Attribute( required = false )
    private String newType;

    @Attribute( required = false )
    private String migrateValue;

    @Attribute( required = false )
    private String reason;

    /**
     * Default constructor
     */
    public KindPropOp()
    {
    }

    /**
     * Constructor used by test cases
     *
     * @param op           the operation to be executed
     * @param kind         the entity kind the operation to be executed on
     * @param property     the entity kind property the operation to be executed on
     * @param defVal       the property default value
     * @param newName      the property new name
     * @param newType      the property new type
     * @param migrateValue the migration value
     * @param reason       the optional reason description
     */
    public KindPropOp( String op, String kind, String property, String type, String defVal,
                       String newName, String newType, String migrateValue, String reason )
    {
        this.op = op;
        this.kind = kind;
        this.property = property;
        this.type = type;
        this.defVal = defVal;
        this.newName = newName;
        this.newType = newType;
        this.migrateValue = migrateValue;
        this.reason = reason;
    }

    /**
     * Returns the operation to be executed.
     *
     * @return the operation
     */
    public String getOperation()
    {
        return op;
    }

    /**
     * Sets the operation to be executed.
     *
     * @param op the operation to be executed
     */
    public void setOperation( String op )
    {
        this.op = op;
    }

    /**
     * Returns the entity kind the operation to be executed on.
     *
     * @return the entity kind
     */
    public String getKind()
    {
        return kind;
    }

    /**
     * Sets the entity kind the operation to be executed on.
     *
     * @param kind the entity kind to be set
     */
    public void setKind( String kind )
    {
        this.kind = kind;
    }

    /**
     * Returns the entity kind property the operation to be executed on.
     *
     * @return the entity kind property
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * Sets the entity kind property the operation to be executed on.
     *
     * @param property the entity kind property
     */
    public void setProperty( String property )
    {
        this.property = property;
    }

    /**
     * Returns the type of the property.
     *
     * @return the type of the property
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the property.
     *
     * @param type the property type to be set
     */
    public void setType( String type )
    {
        this.type = type;
    }

    /**
     * Returns the optional attribute, holding the property default value.
     *
     * @return the property default value
     */
    public String getDefVal()
    {
        return defVal;
    }

    /**
     * Sets the optional attribute, holding the property default value.
     *
     * @param defVal the property default value to be set
     */
    public void setDefVal( String defVal )
    {
        this.defVal = defVal;
    }

    /**
     * Returns the optional attribute, holding the property new name.
     *
     * @return the property new name
     */
    public String getNewName()
    {
        return newName;
    }

    /**
     * Sets the optional attribute, holding the property new name.
     *
     * @param newName the property new name to be set
     */
    public void setNewName( String newName )
    {
        this.newName = newName;
    }

    /**
     * Returns the optional attribute, holding the property new type.
     *
     * @return the property new type
     */
    public String getNewType()
    {
        return newType;
    }

    /**
     * Sets the optional attribute, holding the property new type.
     *
     * @param newType the property new type to be set
     */
    public void setNewType( String newType )
    {
        this.newType = newType;
    }

    /**
     * Returns the optional parameter, whether to migrate old data or not.
     *
     * @return the migrate value
     */
    public String getMigrateValue()
    {
        return migrateValue;
    }

    /**
     * Sets the optional parameter, whether to migrate old data or not.
     *
     * @param migrateValue the migrateValue to be set
     */
    public void setMigrateValue( String migrateValue )
    {
        this.migrateValue = migrateValue;
    }

    /**
     * Returns the optional reason descriptor.
     *
     * @return the reason descriptor
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * Sets the optional reason descriptor.
     *
     * @param reason the reason to be set
     */
    public void setReason( String reason )
    {
        this.reason = reason;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if ( null != op )
        {
            sb.append( "op=" ).append( op );
        }
        if ( null != kind )
        {
            sb.append( " kind=" ).append( kind );
        }
        if ( null != property )
        {
            sb.append( " property=" ).append( property );
        }
        if ( null != type )
        {
            sb.append( " type=" ).append( type );
        }
        if ( null != defVal )
        {
            sb.append( " defVal=" ).append( defVal );
        }
        if ( null != newName )
        {
            sb.append( " newName=" ).append( newName );
        }
        if ( null != newType )
        {
            sb.append( " newType=" ).append( newType );
        }
        if ( null != migrateValue )
        {
            sb.append( " migrateValue=" ).append( migrateValue );
        }
        if ( null != reason )
        {
            sb.append( " reason=" ).append( reason );
        }

        return sb.toString();
    }

}
