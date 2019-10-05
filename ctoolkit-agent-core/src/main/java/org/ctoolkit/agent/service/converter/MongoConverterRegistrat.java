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

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;

/**
 * Converter registrat for mongo db
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">https://docs.mongodb.com/manual/reference/bson-types/</a>
 */
@Singleton
public class MongoConverterRegistrat
        extends BaseConverterRegistrat
{
    public static final String TYPE_STRING = "string";

    public static final String TYPE_LONG = "long";

    public static final String TYPE_DOUBLE = "double";

    public static final String TYPE_DATE = "date";

    public static final String TYPE_BOOL = "bool";

    public static final String TYPE_BIN_DATA = "binData";

    @Override
    public void initialize()
    {
        register( String.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Integer.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Long.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Float.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Double.class, TYPE_STRING, StringConverter.INSTANCE );
        register( BigDecimal.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Boolean.class, TYPE_STRING, StringConverter.INSTANCE );
        register( byte[].class, TYPE_STRING, StringConverter.INSTANCE );
        register( Clob.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Blob.class, TYPE_STRING, StringConverter.INSTANCE );
        register( Date.class, TYPE_STRING, StringConverter.INSTANCE );

        register( String.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Integer.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Long.class, TYPE_LONG, LongConverter.INSTANCE );
        register( Date.class, TYPE_LONG, LongConverter.INSTANCE );

        register( String.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Integer.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Long.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Float.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Double.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( BigDecimal.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );
        register( Date.class, TYPE_DOUBLE, DoubleConverter.INSTANCE );

        register( String.class, TYPE_DATE, DateConverter.INSTANCE );
        register( Long.class, TYPE_DATE, DateConverter.INSTANCE );
        register( Date.class, TYPE_DATE, DateConverter.INSTANCE );

        register( String.class, TYPE_BOOL, BooleanConverter.INSTANCE );
        register( Boolean.class, TYPE_BOOL, BooleanConverter.INSTANCE );

        register( String.class, TYPE_BIN_DATA, BinaryConverter.INSTANCE );
        register( byte[].class, TYPE_BIN_DATA, BinaryConverter.INSTANCE );
        register( Blob.class, TYPE_BIN_DATA, BinaryConverter.INSTANCE );
        register( Clob.class, TYPE_BIN_DATA, BinaryConverter.INSTANCE );
    }
}
