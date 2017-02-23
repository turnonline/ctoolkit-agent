package org.ctoolkit.agent.config;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import org.ctoolkit.agent.rest.AuthorizationInterceptor;
import org.ctoolkit.agent.rest.Authorized;

import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Configuration for IAM module
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class IAMModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindInterceptor(
                Matchers.annotatedWith( Authorized.class ),
                Matchers.annotatedWith( ApiMethod.class ),
                new AuthorizationInterceptor()
        );
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
