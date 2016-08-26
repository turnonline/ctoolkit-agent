package org.ctoolkit.migration.agent.model;

/**
 * Base filter for list base service methods
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class Filter
{
    private int start = 0;

    private int length = 10;

    private String orderBy;

    private boolean ascending;

    public Filter( Builder<?> builder )
    {
        this.start = builder.getStart();
        this.length = builder.getLength();
        this.orderBy = builder.getOrderBy();
        this.ascending = builder.isAscending();
    }

    public int getStart()
    {
        return start;
    }

    public int getLength()
    {
        return length;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public boolean isAscending()
    {
        return ascending;
    }

    public static class Builder<B extends Builder>
    {
        private int start;

        private int length;

        private String orderBy;

        private boolean ascending;

        public B start( int start )
        {
            this.start = start;
            return getThis();
        }

        public B length( int length )
        {
            this.length = length;
            return getThis();
        }

        public B orderBy( String orderBy )
        {
            this.orderBy = orderBy;
            return getThis();
        }

        public B ascending( boolean ascending )
        {
            this.ascending = ascending;
            return getThis();
        }

        protected B getThis()
        {
            return ( B ) this;
        }

        public int getStart()
        {
            return start;
        }

        public int getLength()
        {
            return length;
        }

        public String getOrderBy()
        {
            return orderBy;
        }

        public boolean isAscending()
        {
            return ascending;
        }

        public Filter build()
        {
            return new Filter( this );
        }
    }
}
