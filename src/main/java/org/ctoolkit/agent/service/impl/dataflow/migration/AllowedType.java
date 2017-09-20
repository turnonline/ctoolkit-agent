package org.ctoolkit.agent.service.impl.dataflow.migration;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class AllowedType
{
    private String type;

    private boolean supportList;

    public AllowedType( String type )
    {
        this.type = type;
    }

    public AllowedType( String type, boolean supportList )
    {
        this.type = type;
        this.supportList = supportList;
    }

    public String getType()
    {
        return type;
    }

    public boolean isSupportList()
    {
        return supportList;
    }

    @Override
    public String toString()
    {
        return supportList ? type + ", " + type + " list" : type;
    }
}
