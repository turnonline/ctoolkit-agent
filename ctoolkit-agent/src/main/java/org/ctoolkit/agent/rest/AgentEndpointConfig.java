package org.ctoolkit.agent.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * The endpoint base class as a configuration of the REST API and generated client.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Api( name = "agent",
        canonicalName = "Ctoolkit Agent",
        title = "Cloud Toolkit Migration Agent",
        version = "v1",
        description = "REST API for Cloud Toolkit Migration Agent",
        documentationLink = "https://c-toolkit.appspot.com/docs",
        authenticators = {IAMAuthenticator.class},
        namespace = @ApiNamespace( ownerDomain = "api.ctoolkit.org", ownerName = "Comvai, s.r.o." )
)
public class AgentEndpointConfig
{
}
