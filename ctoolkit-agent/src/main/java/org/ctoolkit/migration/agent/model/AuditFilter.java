package org.ctoolkit.migration.agent.model;

import org.ctoolkit.migration.agent.model.MetadataAudit.Operation;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AuditFilter
{
    private int start = 0;

    private int length = 10;

    private String orderBy;

    private boolean ascending;

    private Operation operation;

    private String ownerId;

    public AuditFilter( Builder builder )
    {
        this.start = builder.getStart();
        this.length = builder.getLength();
        this.orderBy = builder.getOrderBy();
        this.ascending = builder.isAscending();
        this.operation = builder.getOperation();
        this.ownerId = builder.getOwnerId();
    }

    public int getStart()
    {
        return start;
    }

    public void setStart( int start )
    {
        this.start = start;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength( int length )
    {
        this.length = length;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy( String orderBy )
    {
        this.orderBy = orderBy;
    }

    public boolean isAscending()
    {
        return ascending;
    }

    public void setAscending( boolean ascending )
    {
        this.ascending = ascending;
    }

    public Operation getOperation()
    {
        return operation;
    }

    public void setOperation( Operation operation )
    {
        this.operation = operation;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId( String ownerId )
    {
        this.ownerId = ownerId;
    }

    public static class Builder
    {
        private int start = 0;

        private int length = 10;

        private String orderBy;

        private boolean ascending;

        private Operation operation;

        private String ownerId;

        public int getStart()
        {
            return start;
        }

        public Builder setStart( int start )
        {
            this.start = start;
            return this;
        }

        public int getLength()
        {
            return length;
        }

        public Builder setLength( int length )
        {
            this.length = length;
            return this;
        }

        public String getOrderBy()
        {
            return orderBy;
        }

        public Builder setOrderBy( String orderBy )
        {
            this.orderBy = orderBy;
            return this;
        }

        public boolean isAscending()
        {
            return ascending;
        }

        public Builder setAscending( boolean ascending )
        {
            this.ascending = ascending;
            return this;
        }

        public Operation getOperation()
        {
            return operation;
        }

        public Builder setOperation( Operation operation )
        {
            this.operation = operation;
            return this;
        }

        public String getOwnerId()
        {
            return ownerId;
        }

        public Builder setOwnerId( String ownerId )
        {
            this.ownerId = ownerId;
            return this;
        }

        public AuditFilter build()
        {
            return new AuditFilter( this );
        }
    }
}
