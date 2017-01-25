package org.ctoolkit.migration.agent.rest;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsRequest;
import com.google.api.services.cloudresourcemanager.model.TestIamPermissionsResponse;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Injector;
import net.oauth.jsontoken.JsonToken;
import org.ctoolkit.migration.agent.service.RestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static com.google.identitytoolkit.JsonTokenHelper.ID_TOKEN_EMAIL;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class IAMAuthenticator
        implements Authenticator
{
    private static String GTOKEN = "gtoken";

    private Logger log = LoggerFactory.getLogger( IAMAuthenticator.class );

    @Inject
    private static Injector injector;

    @Inject
    private GtokenVerifier verifier;

    @Inject
    private RestContext ctx;

    @Inject
    private CloudResourceManager cloudResourceManager;

    @Override
    public User authenticate( HttpServletRequest request )
    {
        injector.injectMembers( this );

        try
        {
            String gtoken = request.getHeader( GTOKEN );
            if ( gtoken == null )
            {
                log.error( "Header 'gtoken' is not set!" );
            }
            else
            {
                JsonToken token = verifier.verifyAndDeserialize( gtoken );
                JsonObject payloadAsJsonObject = token.getPayloadAsJsonObject();
                JsonElement emailJson = payloadAsJsonObject.get( ID_TOKEN_EMAIL );

                if ( emailJson != null )
                {
                    String email = emailJson.getAsString();

                    // store email to ThreadLocal context - will be used in auditing
                    ctx.setUserEmail( email );

                    // if agent is running on app engine - check permissions
                    if ( SystemProperty.environment.value() == SystemProperty.Environment.Value.Production )
                    {
                        String resource = SystemProperty.applicationId.get();
                        TestIamPermissionsRequest content = new TestIamPermissionsRequest();
                        content.setPermissions( new ArrayList<String>() );
                        content.getPermissions().add( "datastore.entities.create" ); // TODO: is this permission sufficient?

                        CloudResourceManager.Projects.TestIamPermissions testRequest = cloudResourceManager
                                .projects()
                                .testIamPermissions( resource, content );

                        TestIamPermissionsResponse response = testRequest.execute();
                        if ( !response.getPermissions().isEmpty() ) // if user has permission API returns same list as requested
                        {
                            return new User( email );
                        }
                    }
                    // if agent is running in in localhost - skip permission check
                    else
                    {
                        return new User( email );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            log.error( "Unable to verify gtoken", e );
        }

        return null;
    }
}
