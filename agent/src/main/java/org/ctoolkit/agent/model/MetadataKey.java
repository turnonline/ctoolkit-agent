package org.ctoolkit.agent.model;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MetadataKey<M extends BaseMetadata>
{
    private String key;

    private Class<M> metadataClass;

    public MetadataKey( String key, Class<M> metadataClass )
    {
        this.key = key;
        this.metadataClass = metadataClass;
    }

    public String getKey()
    {
        return key;
    }

    public Class<M> getMetadataClass()
    {
        return metadataClass;
    }

    @Override
    public String toString()
    {
        return "MetadataKey{" +
                "key='" + key + '\'' +
                ", metadataClass=" + metadataClass +
                '}';
    }
}
