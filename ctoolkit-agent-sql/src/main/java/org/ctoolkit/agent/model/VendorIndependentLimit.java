package org.ctoolkit.agent.model;

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
