CtoolkiT Agent
==============

Cloud Toolkit Migration Agent REST API - Google AppEngine (Micro) Service

# Important info
To use authenticator for endpoint in local jetty you need to set system property to fake that server is 
running in app engine. Otherwise com.google.api.server.spi.request.Auth#authenticateAppEngineUser()
skips the authentication process:

    -Dcom.google.appengine.runtime.environment=true

