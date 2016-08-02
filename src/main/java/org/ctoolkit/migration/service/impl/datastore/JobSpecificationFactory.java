package org.ctoolkit.migration.service.impl.datastore;

import org.ctoolkit.migration.annotation.ChangeJob;
import org.ctoolkit.migration.annotation.ExportJob;
import org.ctoolkit.migration.annotation.ImportJob;

/**
 * Job specification factory for map reduce
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface JobSpecificationFactory
{
    @ImportJob
    MapSpecificationProvider createImportJobSpecification( String parentKey);

    @ChangeJob
    MapSpecificationProvider createChangeJobSpecification( String parentKey);

    @ExportJob
    MapSpecificationProvider createExportJobSpecification( String parentKey);
}
