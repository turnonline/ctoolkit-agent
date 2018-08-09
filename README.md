CtoolkiT Agent
==============

Cloud Toolkit Migration Agent REST API - Google AppEngine (Micro) Service

# Development
> Important: To use authenticator for endpoint in local jetty you need to set system property to fake that server is 
running in app engine. Otherwise com.google.api.server.spi.request.Auth#authenticateAppEngineUser() skips the authentication process:

    -Dcom.google.appengine.runtime.environment=true
    
To run agent locally execute following command:
     
     mvn jetty:run-exploded
    
# Deployment
Following steps describes what needs to be configured before deploying service to gcloud.
### Enable APIs
First you need to enable these APIs in gcloud console (it requires to enable Billing as well):
- Google Cloud Datastore API (https://console.cloud.google.com/apis/api/datastore.googleapis.com/overview)
- Google Dataflow API (https://console.cloud.google.com/apis/api/dataflow.googleapis.com/overview)
- Google Cloud Resource Manager API (https://console.cloud.google.com/apis/api/cloudresourcemanager.googleapis.com/overview)
- Stackdriver Debugger API (https://console.cloud.google.com/apis/api/clouddebugger.googleapis.com/overview)

    https://console.cloud.google.com/apis/dashboard

### Set project
Set project via CLI into which you want to deploy agent as follows (if not set previously):

    > gcloud config set project <project-name>

### Add role to user
Add role _Cloud datastore Owner_ (https://console.cloud.google.com/iam-admin/iam/project) for user which will call agent API. 
It can be service account or user logged into ctoolkit broker UI

### Deploy to gcloud
Deploy agent to gcloud with following maven command (This step can take several minutes to complete)

    > mvn appengine:deploy

#### Verification
Visit following link and select _Agent_ from service combobox.

    https://console.cloud.google.com/appengine/versions?project=<project-name>
      
There should be visible row with deployed version with _Serving_ status and _Flexible_ Environment. 
You can also visit following link to see if service was deployed successfully:

    https://agent-dot-<project-name>.appspot.com

> Do not forget to kill agent instances when you finish your work with agent, otherwise you will be charged by standard billing. 
(agent is running on flexible environment which does not serve with free quota as opposite to standard environment). You can stop
instances here: https://console.cloud.google.com/appengine/instances?project=<project-name>

## MYSQL setup 
-DjdbcUrl=jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
-DjdbcUsername=root
-DjdbcPassword=admin123
-DjdbcDriver=com.mysql.cj.jdbc.Driver
-DelasticsearchHosts=http://localhost:9200