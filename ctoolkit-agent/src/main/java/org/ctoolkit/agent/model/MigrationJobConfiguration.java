package org.ctoolkit.agent.model;

import java.io.Serializable;

/**
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class MigrationJobConfiguration
        implements Serializable
{
    private static final long serialVersionUID = 5138862517526846988L;

    private String exportId;

    private String importId;

    public MigrationJobConfiguration()
    {
    }

    public MigrationJobConfiguration( String exportId, String importId )
    {
        this.exportId = exportId;
        this.importId = importId;
    }

    public String getExportId()
    {
        return exportId;
    }

    public String getImportId()
    {
        return importId;
    }
}
