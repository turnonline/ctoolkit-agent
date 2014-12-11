package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 *         <p/>
 *         <kindop op="clean" kind="kind" reason="wrong data"/>
 */
@Root
public class KindOp
{

    /**
     * Operations defined on Kinds
     */
    public final static String OP_DROP = "drop";

    public final static String OP_CLEAN = "clean";

    /**
     * Operation
     */
    @Attribute
    private String op;

    /**
     * Operation to execute on
     */
    @Attribute
    private String kind;

    /**
     * Optional reason descriptor
     */
    @Attribute( required = false )
    private String reason;

    /**
     * Default constructor
     */
    public KindOp()
    {
    }

    /**
     * Constructor used by Test cases
     *
     * @param op
     * @param kind
     * @param reason
     */
    public KindOp( String op, String kind, String reason )
    {
        this.op = op;
        this.kind = kind;
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
        return "op: " + op + ", kind: " + kind + ", reason: " + reason;
    }
}
