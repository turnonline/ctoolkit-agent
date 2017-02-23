package org.ctoolkit.agent.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.SecurityUtils;
import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Charsets;
import com.google.inject.assistedinject.Assisted;
import org.ctoolkit.agent.model.CtoolkitAgentConfiguration;
import org.ctoolkit.api.agent.CtoolkitAgent;
import org.ctoolkit.api.agent.CtoolkitAgentScopes;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Ctoolkit agent provider
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
// TODO: this should be handled by resource factory but for now it is not capable to do it (setting rootUrl inside mapreduce job)
// TODO: use different secret.json - one provided is created by c-toolkit project - probably should be provided via database
public class CtoolkitAgentProviderBean
        implements CtoolkitAgentProvider
{
    private String rootUrl;

    private String gtoken;

    @Inject
    public CtoolkitAgentProviderBean( @Assisted CtoolkitAgentConfiguration configuration )
    {
        this.rootUrl = configuration.getRootUrl();
        this.gtoken = configuration.getGtoken();
    }

    @Override
    public CtoolkitAgent get()
    {
        try
        {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            HttpRequestInitializer credential = provideCredential( jsonFactory, httpTransport );

            return new CtoolkitAgent.Builder( httpTransport, jsonFactory, credential )
                    .setApplicationName( "C-toolkit agent" )
                    .setRootUrl( rootUrl )
                    .build();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Unable to create Ctoolkit agent client", e );
        }
    }

    private HttpRequestInitializer provideCredential( JsonFactory jsonFactory, HttpTransport transport )
            throws IOException
    {
        // app engine
        if ( SystemProperty.environment.value() == SystemProperty.Environment.Value.Production )
        {
            return new AppIdentityCredential( CtoolkitAgentScopes.all() );
        }
        // localhost
        else
        {
            InputStream jsonStream = CtoolkitAgentProviderBean.class.getResourceAsStream( "/secret.json" );
            return new ConfiguredByJsonGoogleCredential( jsonStream, jsonFactory, transport ).build();
        }
    }

    private PrivateKey privateKeyFromPkcs8( String privateKeyPem ) throws IOException
    {
        Reader reader = new StringReader( privateKeyPem );
        PemReader.Section section = PemReader.readFirstSectionAndClose( reader, "PRIVATE KEY" );
        if ( section == null )
        {
            throw new IOException( "Invalid PKCS8 data." );
        }

        byte[] bytes = section.getBase64DecodedBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec( bytes );

        try
        {
            KeyFactory keyFactory = SecurityUtils.getRsaKeyFactory();
            return keyFactory.generatePrivate( keySpec );
        }
        catch ( NoSuchAlgorithmException | InvalidKeySpecException e )
        {
            throw new IOException( "Unexpected exception reading PKCS data", e );
        }
    }

    private class ConfiguredByJsonGoogleCredential
            extends GoogleCredential.Builder
    {
        public ConfiguredByJsonGoogleCredential( InputStream jsonStream, JsonFactory jsonFactory, HttpTransport transport )
                throws IOException
        {
            JsonObjectParser parser = new JsonObjectParser( jsonFactory );
            GenericJson fileContents = parser.parseAndClose( jsonStream, Charsets.UTF_8, GenericJson.class );

            String clientEmail = ( String ) fileContents.get( "client_email" );
            String privateKeyPem = ( String ) fileContents.get( "private_key" );

            PrivateKey privateKey = privateKeyFromPkcs8( privateKeyPem );

            // setup credential from json
            setServiceAccountId( clientEmail );
            setServiceAccountPrivateKey( privateKey );
            setServiceAccountScopes( CtoolkitAgentScopes.all() );

            setJsonFactory( jsonFactory );
            setTransport( transport );
        }

        @Override
        public GoogleCredential build()
        {
            return new GoogleCredential( this )
            {
                @Override
                public void intercept( HttpRequest request ) throws IOException
                {
                    super.intercept( request );
                    request.getHeaders().put( "gtoken", gtoken );
                }
            };
        }
    }
}
