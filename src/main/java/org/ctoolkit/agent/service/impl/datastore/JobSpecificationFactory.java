package org.ctoolkit.agent.service.impl.datastore;

import org.ctoolkit.agent.annotation.ImportJob;

/**
 * Job specification factory for map reduce
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface JobSpecificationFactory
{
    @ImportJob
    MapSpecificationProvider createImportJobSpecification( String parentKey);
}
