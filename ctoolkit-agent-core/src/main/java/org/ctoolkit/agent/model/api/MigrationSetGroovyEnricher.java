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

package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@javax.annotation.Generated( value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z" )
public class MigrationSetGroovyEnricher
        extends MigrationSetEnricher
{

    private String command = null;

    /**
     * Groovy shell command. Context map ('ctx' property) will be passed into shell and can be used to write values into context.
     **/
    public MigrationSetGroovyEnricher command( String command )
    {
        this.command = command;
        return this;
    }


    @ApiModelProperty( value = "Groovy shell command. Context map ('ctx' property) will be passed into shell and can be used to write values into context." )
    @JsonProperty( "command" )
    public String getCommand()
    {
        return command;
    }

    public void setCommand( String command )
    {
        this.command = command;
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        MigrationSetGroovyEnricher migrationSetGroovyEnricher = ( MigrationSetGroovyEnricher ) o;
        return Objects.equals( command, migrationSetGroovyEnricher.command );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( command );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "class MigrationSetGroovyEnricher {\n" );

        sb.append( "    command: " ).append( toIndentedString( command ) ).append( "\n" );
        sb.append( "}" );
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString( Object o )
    {
        if ( o == null )
        {
            return "null";
        }
        return o.toString().replace( "\n", "\n    " );
    }
}

