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

package biz.turnonline.ecosystem.service.converter;

import biz.turnonline.ecosystem.model.RawKey;
import biz.turnonline.ecosystem.model.api.ImportSetProperty;
import biz.turnonline.ecosystem.model.api.MigrationSet;
import biz.turnonline.ecosystem.model.api.MigrationSetEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetEnricherGroup;
import biz.turnonline.ecosystem.model.api.MigrationSetProperty;
import biz.turnonline.ecosystem.service.enricher.EnricherExecutor;
import biz.turnonline.ecosystem.service.transformer.TransformerExecutor;
import biz.turnonline.ecosystem.service.transformer.TransformerProcessor;
import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ConverterExecutor
{
    private static final String ID_ENCODE_PREFIX = "encode:";

    private TransformerExecutor transformerExecutor;

    private EnricherExecutor enricherExecutor;

    private ConverterRegistrat registrat;

    private final Map<String, Object> ctx = new HashMap<>();

    public ConverterExecutor()
    {
    }

    public ConverterExecutor( ConverterRegistrat registrat )
    {
        this.registrat = registrat;
    }

    public ConverterExecutor( EnricherExecutor enricherExecutor, TransformerExecutor transformerExecutor, ConverterRegistrat registrat )
    {
        this.enricherExecutor = enricherExecutor;
        this.transformerExecutor = transformerExecutor;
        this.registrat = registrat;
    }

    public void enrich( Map<String, Object> ctx, List<MigrationSetEnricherGroup> groups )
    {
        if ( groups == null )
        {
            return;
        }

        groups.forEach( group -> {
            Stream<MigrationSetEnricher> stream;

            if ( "parallel".equals( group.getExecution() ) )
            {
                stream = group.getEnrichers().parallelStream();
            }
            else
            {
                stream = group.getEnrichers().stream();
            }

            stream.forEach( enricher -> {
                // enrich
                enricherExecutor.enrich( enricher, ctx );

                // recursive call for complex enricher structures
                enrich( ctx, enricher.getEnricherGroups() );
            } );
        } );
    }

    public ImportSetProperty convertProperty( Object source, MigrationSetProperty property )
    {
        // value is hardcoded
        if ( property.getTargetValue() != null )
        {
            ImportSetProperty importSetProperty = newImportSetProperty( property );
            importSetProperty.setValue( property.getTargetValue() );

            return importSetProperty;
        }

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

    public String convertId( MigrationSet migrationSet, Map<String, Object> ctx )
    {
        // for GCP datastore
        RawKey key = ( RawKey ) ctx.get( "__key__" );
        if ( key != null )
        {
            return key.toString();
        }

        String idSelectorRaw = migrationSet.getSource().getIdSelector();
        if ( idSelectorRaw == null )
        {
            return null;
        }

        boolean encode = idSelectorRaw.startsWith( ID_ENCODE_PREFIX );

        StringSubstitutor substitution = new StringSubstitutor( ctx, "${", "}" );
        String id = substitution.replace( idSelectorRaw.replaceAll( ID_ENCODE_PREFIX, "" ) );

        if ( encode )
        {
            id = BaseEncoding.base64().encode( id.getBytes( Charsets.UTF_8 ) );
        }

        return id;
    }

    public void putToContext( MigrationSet migrationSet )
    {
        ctx.put( MigrationSet.class.getName(), migrationSet );

        ctx.put( "source.namespace", migrationSet.getSource().getNamespace() );
        ctx.put( "source.kind", migrationSet.getSource().getKind() );

        ctx.put( "target.namespace", migrationSet.getTarget().getNamespace() );
        ctx.put( "target.kind", migrationSet.getTarget().getKind() );
    }

    public Map<String, Object> getCtx()
    {
        return ctx;
    }

    // -- private helpers

    private ImportSetProperty newImportSetProperty( MigrationSetProperty property )
    {
        ImportSetProperty importSetProperty = new ImportSetProperty();
        importSetProperty.setName( Optional.ofNullable( property.getTargetProperty() ).orElse( property.getSourceProperty() ) );
        importSetProperty.setType( property.getTargetType() );

        return importSetProperty;
    }
}
