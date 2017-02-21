package org.ctoolkit.migration.agent.model;

/**
 * Base filter for list base service methods
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class BaseMetadataFilter<M extends BaseMetadata>
{
    private int start = 0;

    private int length = 10;

    private String orderBy;

    private boolean ascending;

    private Class<M> metadataClass;

    public BaseMetadataFilter( Builder<?, M> builder )
    {
        this.start = builder.getStart();
        this.length = builder.getLength();
        this.orderBy = builder.getOrderBy();
        this.ascending = builder.isAscending();
        this.metadataClass = builder.getMetadataClass();
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

    public Class<M> getMetadataClass()
    {
        return metadataClass;
    }

    public static class Builder<B extends Builder, M extends BaseMetadata>
    {
        private int start;

        private int length;

        private String orderBy;

        private boolean ascending;

        private Class<M> metadataClass;

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

        public B metadataClass( Class<M> metadataClass )
        {
            this.metadataClass = metadataClass;
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

        public Class<M> getMetadataClass()
        {
            return metadataClass;
        }

        public BaseMetadataFilter build()
        {
            return new BaseMetadataFilter( this );
        }
    }
}
