package org.ctoolkit.agent.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The bean holding migration set kind rule
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class MigrationSetKindOpRule
{
    public final static String OP_EQ = "eq";
    public final static String OP_LT = "lt";
    public final static String OP_LTE = "lte";
    public final static String OP_GT = "gt";
    public final static String OP_GTE = "gte";
    public final static String OP_REGEXP = "regexp";

    @XmlAttribute( name = "property" )
    private String property;

    @XmlAttribute( name = "op" )
    private String op;

    @XmlAttribute( name = "value" )
    private String value;

    public String getProperty()
    {
        return property;
    }

    public void setProperty( String property )
    {
        this.property = property;
    }

    public String getOp()
    {
        return op;
    }

    public void setOp( String op )
    {
        this.op = op;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "MigrationSetKindOpRule{" +
                "property='" + property + '\'' +
                ", op='" + op + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
