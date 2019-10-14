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

package biz.turnonline.ecosystem.model;

import java.util.LinkedHashMap;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Context
        extends LinkedHashMap<String, Object>
{
    public Context()
    {
    }

    public Context( Context context)
    {
        putAll( context );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "\n" );

        this.forEach( ( name, value ) ->
                sb.append( name ).append( " -> " )
                        .append( "'" ).append( value ).append( "'" )
                        .append( " (" ).append( value.getClass().getName() ).append( ")" )
                        .append( "\n" ) );

        sb.append( "==================================================\n" );

        return sb.toString();
    }
}
