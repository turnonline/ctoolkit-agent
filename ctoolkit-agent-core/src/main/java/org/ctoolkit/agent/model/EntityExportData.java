package org.ctoolkit.agent.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity export data holds information about exported entity
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class EntityExportData
        implements Serializable
{
    private String id;

    private Map<String, Property> properties = new HashMap<>();

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public Map<String, Property> getProperties()
    {
        return properties;
    }

    public void setProperties( Map<String, Property> properties )
    {
        this.properties = properties;
    }

    public static class Property
            implements Serializable
    {
        private Object value;

        public Property()
        {
        }

        public Property( Object value )
        {
            this.value = value;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue( Object value )
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "Property{" +
                    "value=" + value +
                    '}';
        }
    }

    @Override
    public String toString()
    {
        return "EntityMetaData{" +
                "properties=" + properties +
                '}';
    }
}
