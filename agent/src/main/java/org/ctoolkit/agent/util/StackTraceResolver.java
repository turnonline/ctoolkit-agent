/*
 * Copyright (c) 2017 Comvai, s.r.o. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ctoolkit.agent.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility for resolving stack trace from exception
 * <p>
 * Created by Jozef Pohorelec on 6. 8. 2015.
 */
public final class StackTraceResolver
{
    private StackTraceResolver()
    {
    }

    /**
     * Resolve error stack trace
     *
     * @param e exception to resolve
     * @return stack trace as string
     */
    public static String resolve( Throwable e )
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw );

        e.printStackTrace( pw );

        return sw.toString();
    }
}
