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

package org.ctoolkit.agent.service.sql;

import org.ctoolkit.agent.model.api.QueryFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class Query
{
    private String namespace = null;

    private String kind = null;

    private int limit = 0;

    private int offset = 0;

    private List<QueryFilter> filter = new ArrayList<>();

    public Query( )
    {
    }

    public Query( Query query )
    {
        this.namespace = query.getNamespace();
        this.kind = query.getKind();
        this.limit = query.getLimit();
        this.offset = query.getOffset();

        this.filter = new ArrayList<>( query.getFilter() );
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind( String kind )
    {
        this.kind = kind;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit( int limit )
    {
        this.limit = limit;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset( int offset )
    {
        this.offset = offset;
    }

    public List<QueryFilter> getFilter()
    {
        return filter;
    }

    public void setFilter( List<QueryFilter> filter )
    {
        this.filter = filter;
    }
}
