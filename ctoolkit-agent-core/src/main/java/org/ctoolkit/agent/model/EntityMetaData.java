package org.ctoolkit.agent.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity metadata holds information about entity
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class EntityMetaData
        implements Serializable
{
    private Map<String, Property> properties = new HashMap<>();

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

        private String typeName;

        private String className;

        public Object getValue()
        {
            return value;
        }

        public void setValue( Object value )
        {
            this.value = value;
        }

        public String getTypeName()
        {
            return typeName;
        }

        public void setTypeName( String typeName )
        {
            this.typeName = typeName;
        }

        public String getClassName()
        {
            return className;
        }

        public void setClassName( String className )
        {
            this.className = className;
        }

        @Override
        public String toString()
        {
            return "Property{" +
                    "value=" + value +
                    ", typeName='" + typeName + '\'' +
                    ", className='" + className + '\'' +
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