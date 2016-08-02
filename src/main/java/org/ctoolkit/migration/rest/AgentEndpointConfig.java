package org.ctoolkit.migration.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * The endpoint base class as a configuration of the REST API and generated client.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api( name = "migration",
        canonicalName = "Ctoolkit Agent",
        title = "Ctoolkit Agent API",
        version = "v1",
        description = "REST API for cloud datastore data migration management.",
        documentationLink = "https://c-toolkit.appspot.com/docs",
        namespace = @ApiNamespace( ownerDomain = "ctoolkit.org", ownerName = "Comvai, s.r.o." )
)
public class AgentEndpointConfig
{
}
