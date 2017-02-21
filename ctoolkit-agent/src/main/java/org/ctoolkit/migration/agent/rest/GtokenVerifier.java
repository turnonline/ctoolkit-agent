package org.ctoolkit.migration.agent.rest;

import com.google.gson.JsonObject;
import com.google.identitytoolkit.GitkitVerifierManager;
import com.google.identitytoolkit.JsonTokenHelper;
import com.google.identitytoolkit.RpcHelper;
import net.oauth.jsontoken.Checker;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.discovery.VerifierProviders;

import java.security.SignatureException;

/**
 * Custom implementation of gtoken verification
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 * @see {@link JsonTokenHelper}
 */
public class GtokenVerifier
{
    private final JsonTokenParser parser;

    public GtokenVerifier( RpcHelper rpcHelper, String... audiences )
    {
        VerifierProviders verifierProviders = new VerifierProviders();
        verifierProviders.setVerifierProvider( SignatureAlgorithm.RS256, new GitkitVerifierManager( rpcHelper ) );
        parser = new JsonTokenParser( verifierProviders, new AudienceChecker( audiences ) );
    }

    public JsonToken verifyAndDeserialize( String token ) throws SignatureException
    {
        return parser.verifyAndDeserialize( token );
    }

    private class AudienceChecker
            implements Checker
    {
        private String[] audiences;

        private AudienceChecker( String[] audiences )
        {
            this.audiences = audiences;
        }

        @Override
        public void check( JsonObject payload ) throws SignatureException
        {
            // TODO: should we check audience(project id)?
            // TODO: -audience can vary if user wants to export from one project to another
            // TODO: -what should be source of expected audiences - database?
        }
    }
}
