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

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class RawKey implements Serializable
{
    private String rawKey;

    public RawKey( String rawKey )
    {
        this.rawKey = rawKey;
    }

    public String getRawKey()
    {
        return rawKey;
    }

    public void setRawKey( String rawKey )
    {
        this.rawKey = rawKey;
    }

    @Override
    public String toString()
    {
        return rawKey;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( !( o instanceof RawKey ) ) return false;
        RawKey rawKey1 = ( RawKey ) o;
        return Objects.equals( rawKey, rawKey1.rawKey );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( rawKey );
    }
}
