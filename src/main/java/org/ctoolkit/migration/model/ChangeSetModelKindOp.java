package org.ctoolkit.migration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The bean holding entity kind model update descriptor.
 * Examples:
 * <pre>
 * {@code
 * <kindop op="clean" kind="kind" reason="wrong data"/>
 * }
 * </pre>
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ChangeSetModelKindOp
{
    /**
     * Operations defined on entity kinds
     */
    public final static String OP_DROP = "drop";

    public final static String OP_CLEAN = "clean";

    @XmlAttribute(name = "op")
    private String op;

    @XmlAttribute(name = "kind")
    private String kind;

    public String getOp()
    {
        return op;
    }

    public void setOp( String op )
    {
        this.op = op;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    @Override
    public String toString()
    {
        return "KindOp{" +
                "op='" + op + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}
