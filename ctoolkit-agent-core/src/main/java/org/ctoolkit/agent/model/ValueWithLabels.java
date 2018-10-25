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

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value with labels adds prefixed labelled values to value.
 * For instance <pre>new ValueWithLabels(1).addLabel("name","__syncId").toString()</pre> will produce <pre>[name=__syncId]1</pre>.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ValueWithLabels
{
    private static final String REGEX_GROUP_LABEL = "\\[(\\w+):(\\w+)]";

    private static final String REGEX_GROUP_VALUE = ".*](\\w+)";

    private Map<String, String> labels = new TreeMap<>();

    private Object value;

    public ValueWithLabels( Object value )
    {
        this.value = value;
    }

    public static ValueWithLabels of( String value )
    {
        ValueWithLabels valueWithLabels = new ValueWithLabels( value );

        // retrieve value
        Matcher matcherValue = Pattern.compile( REGEX_GROUP_VALUE ).matcher( value );
        while ( matcherValue.find() )
        {
            valueWithLabels = new ValueWithLabels( matcherValue.group( 1 ) );

            // retrieve label
            Matcher matcherLabel = Pattern.compile( REGEX_GROUP_LABEL ).matcher( value );
            while ( matcherLabel.find() )
            {
                valueWithLabels.addLabel( matcherLabel.group( 1 ), matcherLabel.group( 2 ) );
            }
        }

        return valueWithLabels;
    }

    public ValueWithLabels addLabel( String key, String value )
    {
        labels.put( key, value );
        return this;
    }

    public Map<String, String> getLabels()
    {
        return labels;
    }

    public Object getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for ( Map.Entry<String, String> entry : labels.entrySet() )
        {
            sb.append( "[" );
            sb.append( entry.getKey() );
            sb.append( ":" );
            sb.append( entry.getValue() );
            sb.append( "]" );
        }

        sb.append( value );

        return sb.toString();
    }
}
