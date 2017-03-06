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

package org.ctoolkit.agent.service.impl.datastore;

import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.agent.annotation.ChangeJob;
import org.ctoolkit.agent.annotation.ExportJob;
import org.ctoolkit.agent.annotation.ImportJob;
import org.ctoolkit.agent.annotation.MigrateJob;
import org.ctoolkit.agent.model.MigrationJobConfiguration;

/**
 * Job specification factory for map reduce
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface JobSpecificationFactory
{
    @ImportJob
    MapSpecificationProvider createImportJobSpecification( String parentKey );

    @ChangeJob
    MapSpecificationProvider createChangeJobSpecification( String parentKey );

    @ExportJob
    MapSpecificationProvider createExportJobSpecification( String parentKey );

    @MigrateJob
    MapSpecificationProvider createMigrateJobSpecification( MigrationJobConfiguration jobConfiguration,
                                                            @Assisted( "agentUrl" ) String agentUrl,
                                                            @Assisted( "token" ) String token );
}
