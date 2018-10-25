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

package org.ctoolkit.agent;

import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetSource;
import org.ctoolkit.agent.model.api.MigrationSetTarget;

/**
 * Mocks for unit tests
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Mocks
{
    public static EntityExportData exportData( String name, Object value )
    {
        EntityExportData exportData = new EntityExportData();
        exportData.getProperties().put( name, new EntityExportData.Property( value ) );

        return exportData;
    }

    public static MigrationSetSource migrationSetSource( String namespace, String kind )
    {
        MigrationSetSource source = new MigrationSetSource();
        source.setNamespace( namespace );
        source.setKind( kind );

        return source;
    }

    public static MigrationSetTarget migrationSetTarget( String namespace, String kind )
    {
        MigrationSetTarget target = new MigrationSetTarget();
        target.setNamespace( namespace );
        target.setKind( kind );

        return target;
    }

    public static MigrationSetProperty mockMigrationSetProperty( String type )
    {
        MigrationSetProperty property = new MigrationSetProperty();
        property.setTargetType( type );
        property.setTargetProperty( "name" );
        return property;
    }

    public static ImportSetProperty mockImportSetProperty( String type, String value )
    {
        ImportSetProperty property = new ImportSetProperty();
        property.setType( type );
        property.setValue( value );
        return property;
    }
}
