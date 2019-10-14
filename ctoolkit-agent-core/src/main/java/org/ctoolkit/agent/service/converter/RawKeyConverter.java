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

package org.ctoolkit.agent.service.converter;

import org.ctoolkit.agent.model.RawKey;
import org.ctoolkit.agent.model.api.ImportSetProperty;
import org.ctoolkit.agent.model.api.MigrationSetProperty;

/**
 * RawKey converter
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RawKeyConverter
        implements Converter
{
    public static RawKeyConverter INSTANCE = new RawKeyConverter();

    @Override
    public String convert( Object source, MigrationSetProperty property )
    {
        return source != null ? source.toString() : null;
    }

    @Override
    public RawKey convert( ImportSetProperty property )
    {
        return property.getValue() != null ? new RawKey( ( String ) property.getValue() ) : null;
    }
}
