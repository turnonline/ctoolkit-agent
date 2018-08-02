package org.ctoolkit.agent.model;

import net.sf.jsqlparser.statement.select.AllColumns;

/**
 * Count the columns (as in "SELECT count(1) FROM ...")
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class CountColumn extends AllColumns
{
    @Override
    public String toString()
    {
        return "count(1)";
    }
}
