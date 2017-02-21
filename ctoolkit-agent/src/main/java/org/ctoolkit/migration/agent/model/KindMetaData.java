package org.ctoolkit.migration.agent.model;

/**
 * Metadata for entity kind
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class KindMetaData
{
    private String kind;

    private String namespace;

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    }

    @Override
    public String toString()
    {
        return "KindMetaData{" +
                "kind='" + kind + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
