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

package org.ctoolkit.agent.converter;

import org.apache.beam.repackaged.beam_runners_core_java.com.google.common.base.Charsets;
import org.apache.beam.repackaged.beam_runners_core_java.com.google.common.io.BaseEncoding;
import org.apache.commons.text.StringSubstitutor;
import org.ctoolkit.agent.model.EntityExportData;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetProperty;
import org.ctoolkit.agent.transformer.TransformerExecutor;
import org.ctoolkit.agent.transformer.TransformerProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ConverterExecutor
{
    private TransformerExecutor transformerExecutor;

    private ConverterRegistrat registrat;

    private final Map<Object, Object> ctx = new HashMap<>();

    public ConverterExecutor()
    {
    }

    public ConverterExecutor( ConverterRegistrat registrat )
    {
        this.registrat = registrat;
    }

    public ConverterExecutor( TransformerExecutor transformerExecutor, ConverterRegistrat registrat )
    {
        this.transformerExecutor = transformerExecutor;
        this.registrat = registrat;
    }

    public ImportSetProperty convertProperty( Object source, MigrationSetProperty property )
    {
        // apply transformers to source value
        source = transformerExecutor.transform( source, property.getTransformers(), ctx, TransformerProcessor.Phase.PRE_CONVERT.value() );

        // get converter for source/target type combination
        Converter converter = registrat.get( source, property.getTargetType() );

        // if converter exists for source/target type combination apply conversion + post convert transformers
        if ( converter != null )
        {
            // convert transformed source to target string value
            String target = converter.convert( source, property );

            // apply transformers to converted string value
            target = transformerExecutor.transform( target, property.getTransformers(), ctx, TransformerProcessor.Phase.POST_CONVERT.value() );

            ImportSetProperty importSetProperty = newImportSetProperty( property );
            importSetProperty.setValue( target );

            return importSetProperty;
        }

        return null;
    }

    public Object convertProperty( ImportSetProperty importSetProperty )
    {
        Converter converter = registrat.get( importSetProperty.getType() );
        if ( converter != null )
        {
            return converter.convert( importSetProperty );
        }

        return null;
    }

    public String convertId( MigrationSet migrationSet, EntityExportData entityExportData )
    {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put( "target.namespace", migrationSet.getTarget().getNamespace() );
        placeholders.put( "target.kind", migrationSet.getTarget().getKind() );

        for ( Map.Entry<String, EntityExportData.Property> entry : entityExportData.getProperties().entrySet() )
        {
            Object value = entry.getValue().getValue();
            if ( value != null )
            {
                placeholders.put( "source." + entry.getKey(), value.toString() );
            }
        }

        StringSubstitutor substitution = new StringSubstitutor( placeholders, "{", "}" );
        String id = substitution.replace( migrationSet.getSource().getIdPattern() );

        if ( migrationSet.getSource().getEncodeId() )
        {
            id = BaseEncoding.base64().encode( id.getBytes( Charsets.UTF_8 ) );
        }

        return id;
    }

    public void putToContext( Object key, Object value )
    {
        ctx.put( key, value );
    }

    private ImportSetProperty newImportSetProperty( MigrationSetProperty property )
    {
        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( property.getTargetProperty() );
        importSetProperty.setType( property.getTargetType() );

        return importSetProperty;
    }
}
