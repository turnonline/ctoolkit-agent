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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link ValueWithLabels}
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ValueWithLabelsTest
{
    // -- test toString()

    @Test
    public void testToStringNoLabel()
    {
        ValueWithLabels value = new ValueWithLabels( "12" );
        assertEquals( "12", value.toString() );
    }

    @Test
    public void testToStringOneLabel()
    {
        ValueWithLabels value = new ValueWithLabels( "12" );
        value.addLabel( "name", "_syncId" );

        assertEquals( "[name:_syncId]12", value.toString() );
    }

    @Test
    public void testToStringTwoLabels()
    {
        ValueWithLabels value = new ValueWithLabels( "12" );
        value.addLabel( "name", "_syncId" );
        value.addLabel( "lookup", "clientId" );

        assertEquals( "[lookup:clientId][name:_syncId]12", value.toString() );
    }

    // -- test of()

    @Test
    public void testOfNoLabel()
    {
        ValueWithLabels value = ValueWithLabels.of( "12" );

        assertEquals( "12", value.getValue() );
        assertTrue( value.getLabels().isEmpty() );
    }

    @Test
    public void testOfOneLabel()
    {
        ValueWithLabels value = ValueWithLabels.of( "[name:_syncId]12" );

        assertEquals( "12", value.getValue() );
        assertEquals( 1, value.getLabels().size() );
        assertEquals( "_syncId", value.getLabels().get( "name" ) );
    }

    @Test
    public void testOfTwoLabels()
    {
        ValueWithLabels value = ValueWithLabels.of( "[lookup:clientId][name:_syncId]12" );

        assertEquals( "12", value.getValue() );
        assertEquals( 2, value.getLabels().size() );
        assertEquals( "_syncId", value.getLabels().get( "name" ) );
        assertEquals( "clientId", value.getLabels().get( "lookup" ) );
    }
}