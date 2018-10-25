/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
