package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

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
@Root
public class KindOp
{
    /**
     * Operations defined on entity kinds
     */
    public final static String OP_DROP = "drop";

    public final static String OP_CLEAN = "clean";

    @Attribute
    private String op;

    @Attribute
    private String kind;

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
     * @param op     the operation to be executed
     * @param kind   the entity kind the operation to be executed on
     * @param reason the optional reason description
     */
    public KindOp( String op, String kind, String reason )
    {
        this.op = op;
        this.kind = kind;
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
        return "op: " + op + ", kind: " + kind + ", reason: " + reason;
    }
}
