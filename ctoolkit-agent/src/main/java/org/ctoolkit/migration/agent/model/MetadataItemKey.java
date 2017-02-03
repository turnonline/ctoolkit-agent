package org.ctoolkit.migration.agent.model;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class MetadataItemKey<MI extends BaseMetadataItem>
{
    private String key;

    private Class<MI> metadataItemClass;

    public MetadataItemKey( String key, Class<MI> metadataItemClass )
    {
        this.key = key;
        this.metadataItemClass = metadataItemClass;
    }

    public String getKey()
    {
        return key;
    }

    public Class<MI> getMetadataItemClass()
    {
        return metadataItemClass;
    }

    @Override
    public String toString()
    {
        return "MetadataItemKey{" +
                "key='" + key + '\'' +
                ", metadataItemClass=" + metadataItemClass +
                '}';
    }
}
