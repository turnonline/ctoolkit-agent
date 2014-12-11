package org.ctoolkit.bulkloader.common;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ExportResult
{

    private Long result;

    public ExportResult()
    {
        this.result = BulkLoaderConstants.BULKLOADER_RESULT_OK;
    }

    public Long getResult()
    {
        return result;
    }

    public void setResult( Long result )
    {
        this.result = result;
    }
}
