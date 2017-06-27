package org.ctoolkit.agent.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The bean holding migration set kind operation restrictions
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class MigrationSetKindOpRuleSet
{
    public static final String RESTRICTION_VALUE_AND = "and";
    public static final String RESTRICTION_VALUE_OR = "or";

    @XmlAttribute( name = "op" )
    private String op;

    @XmlElement( name = "rule" )
    private List<MigrationSetKindOpRule> rules;

    public String getOp()
    {
        return op;
    }

    public void setOp( String op )
    {
        this.op = op;
    }

    public List<MigrationSetKindOpRule> getRules()
    {
        if ( rules == null )
        {
            rules = new ArrayList<>();
        }
        return rules;
    }

    public void setRules( List<MigrationSetKindOpRule> rules )
    {
        this.rules = rules;
    }

    @Override
    public String toString()
    {
        return "MigrationSetKindOpRestriction{" +
                ", op='" + op + '\'' +
                ", rules=" + rules +
                '}';
    }
}
