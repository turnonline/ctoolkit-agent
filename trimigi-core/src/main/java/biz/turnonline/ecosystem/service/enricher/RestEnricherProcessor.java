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

package biz.turnonline.ecosystem.service.enricher;

import biz.turnonline.ecosystem.model.api.MigrationSetEnricher;
import biz.turnonline.ecosystem.model.api.MigrationSetRestEnricher;
import biz.turnonline.ecosystem.model.api.QueryParameter;
import biz.turnonline.ecosystem.service.connector.ConnectorFacade;
import com.github.wnameless.json.flattener.JsonFlattener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MigrationSetEnricher} enricher
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class RestEnricherProcessor
        implements EnricherProcessor<MigrationSetRestEnricher>
{
    @Inject
    private ConnectorFacade connectorFacade;

    @Override
    public void enrich( MigrationSetRestEnricher enricher, Map<String, Object> ctx )
    {
        Map<String, String> queryParameters = new HashMap<>();

        enricher.getQueryParameters().forEach( queryParameter -> {
            Object value = queryParameterValue( queryParameter, ctx );
            String converter = queryParameter.getConverter();

            String converted = convert( value, converter );
            queryParameters.put( queryParameter.getName(), converted );
        } );

        String response = ( String ) connectorFacade.pull( enricher.getUrl(), queryParameters );
        Map<String, Object> flattenedResponse = JsonFlattener.flattenAsMap( response );
        flattenedResponse.forEach( ( key, value ) -> ctx.put( prefix( enricher ) + key, value ) );
    }

    // -- private helpers

    private String convert( Object param, String converter )
    {
        if ( param == null )
        {
            return null;
        }

        switch ( converter )
        {
            case "date":
            {
                return new SimpleDateFormat( "yyyy-MM-dd" ).format( param );
            }
            case "datetime":
            {
                return new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSX" ).format( param );
            }
            default:
            {
                return param.toString();
            }
        }
    }

    private String prefix( MigrationSetRestEnricher enricher )
    {
        if ( enricher.getName() != null )
        {
            return enricher.getName() + ".";
        }

        return "";
    }

    private Object queryParameterValue( QueryParameter queryParameter, Map<String, Object> ctx )
    {
        String val = queryParameter.getValue();
        if ( val.startsWith( "${" ) )
        {
            return ctx.get( val.replace( "${", "" ).replace( "}", "" ) );
        }

        return val;
    }
}
