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

import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

/**
 * Vendor independent limit
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
// TODO: support for all databases - currently supports only MySQL
public class VendorIndependentLimit
        extends SelectVisitorAdapter
{
    private final int offset;

    private final int rows;

    public VendorIndependentLimit( int offset, int rows )
    {
        this.offset = offset;
        this.rows = rows;
    }

    @Override
    public void visit( PlainSelect plainSelect )
    {
        Limit limit = new Limit();
        limit.setOffset( offset * rows );
        limit.setRowCount( rows );
        plainSelect.setLimit( limit );
    }
}