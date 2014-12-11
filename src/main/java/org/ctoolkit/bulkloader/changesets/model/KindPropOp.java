package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         Bean holding kind property model update description
 *         <p/>
 *         Example:
 *         <kindprop op="update" kind="kind" property="prop" defval="" newname="" newtype="" migratevalue="" reason="blabla"/>
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

    /**
     * Operation
     */
    @Attribute
    private String op;

    /**
     * Kind to execute on
     */
    @Attribute
    private String kind;

    /**
     * Kind property to execute on
     */
    @Attribute
    private String property;

    /**
     * Type of the property
     * can be one of ChangeSetEntityProperty.PROPERTY_TYPE...
     */
    @Attribute( required = false )
    private String type;

    /**
     * Optional attribute, holding the properties default value
     */
    @Attribute( required = false )
    private String defVal;

    /**
     * Optional attribute, holding the properties new name
     */
    @Attribute( required = false )
    private String newName;

    /**
     * Optional attribute, holding the properties new type
     */
    @Attribute( required = false )
    private String newType;

    /**
     * Optional parameter, to migrate old data or don't
     */
    @Attribute( required = false )
    private String migrateValue;

    /**
     * Optional reason descriptor
     */
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
     * @param op
     * @param kind
     * @param property
     * @param defVal
     * @param newName
     * @param newType
     * @param migrateData
     * @param reason
     */
    public KindPropOp( String op, String kind, String property, String type, String defVal,
                       String newName, String newType, String migrateData, String reason )
    {
        this.op = op;
        this.kind = kind;
        this.property = property;
        this.type = type;
        this.defVal = defVal;
        this.newName = newName;
        this.newType = newType;
        this.migrateValue = migrateData;
        this.reason = reason;
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
     * @return the property
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty( String property )
    {
        this.property = property;
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
     * @return the defVal
     */
    public String getDefVal()
    {
        return defVal;
    }

    /**
     * @param defVal the defVal to set
     */
    public void setDefVal( String defVal )
    {
        this.defVal = defVal;
    }

    /**
     * @return the newName
     */
    public String getNewName()
    {
        return newName;
    }

    /**
     * @param newName the newName to set
     */
    public void setNewName( String newName )
    {
        this.newName = newName;
    }

    /**
     * @return the newType
     */
    public String getNewType()
    {
        return newType;
    }

    /**
     * @param newType the newType to set
     */
    public void setNewType( String newType )
    {
        this.newType = newType;
    }

    /**
     * @return the migrateValue
     */
    public String getMigrateValue()
    {
        return migrateValue;
    }

    /**
     * @param migrateValue the migrateValue to set
     */
    public void setMigrateValue( String migrateValue )
    {
        this.migrateValue = migrateValue;
    }

    /**
     * @return the reason
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason( String reason )
    {
        this.reason = reason;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
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
