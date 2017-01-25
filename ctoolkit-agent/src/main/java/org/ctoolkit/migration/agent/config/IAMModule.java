package org.ctoolkit.migration.agent.config;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.identitytoolkit.HttpSender;
import com.google.identitytoolkit.RpcHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.ctoolkit.migration.agent.rest.GtokenVerifier;

import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Required instances for IAM module
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class IAMModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
    }

    @Provides
    @Singleton
    GtokenVerifier provideGtokenVerifier( RpcHelper rpcHelper )
    {
        return new GtokenVerifier( rpcHelper);
    }

    @Provides
    @Singleton
    RpcHelper provideRpcHelper( Injector injector )
    {
        HttpSender sender = injector.getInstance( HttpSender.class );
        return new RpcHelper( sender, "https://www.googleapis.com/identitytoolkit/v3/relyingparty/", null, null );
    }

    @Provides
    @Singleton
    CloudResourceManager provideCloudResourceManager()
            throws GeneralSecurityException, IOException
    {
        AppIdentityCredential credential = new AppIdentityCredential(
                Collections.singletonList( "https://www.googleapis.com/auth/cloud-platform" )
        );

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        return new CloudResourceManager.Builder( httpTransport, jsonFactory, credential )
                .setApplicationName( "C-toolkit agent" )
                .build();
    }
}
