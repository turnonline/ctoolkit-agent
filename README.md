CtoolkiT Agent
==============

Cloud Toolkit Migration Agent REST API (Micro) Service is tool which motivation is to be able migrate data from 
any to any data source. For every datasource there is written specific agent which knows how to import and export data
from it.

# How it works
![how-it-works](https://github.com/turnonline/ctoolkit-agent/blob/feature/multiple-agents/etc/how-it-works.png)

## Project structure
* ctoolkit-agent-core - shared core for migration agents. It handles core busines logic like transformations, conversions, rule sets, beam creation and so on
* ctoolkit-agent-elasticsearch - concrete agent implementation for elasticsearch data source
* ctoolkit-agent-mongo - concrete agent implementation for mongo data source
* ctoolkit-agent-sql - concrete agent implememtation for sql data sources

## Docker swarm setup
Recommended way of running agents is via docker. To setup docker follow these steps:
* download docker machine - https://docs.docker.com/machine/install-machine/#install-machine-directly
* create docker machine - https://docs.docker.com/machine/reference/create/ - give machine name 'morty'
* start docker machine - to start docker machine run following command:
```shell
# docker-machine start morty
```
* add record to /etc/hosts for your docker machine - `#172.16.139.211 morty` - to find out what is ip of your docker machine run following command:
```shell
# docker-machine ls
```
* create network 'migration' in docker - to add network run following command:
```shell
# docker network create --scope swarm —driver overlay migration
```
* add docker testing stacks - go to 'docker' directory and run one of the following commands (depending of migrating stack):

> SQL (postgres) to elasticsearch migration stack:

```shell
# docker stack deploy -c docker-compose-postgres.yml -c docker-compose-elasticsearch.yml -c docker-compose-agent-sql.yml -c docker-compose-agent-elasticsearch.yml migration 

```
> SQL (postgres) to mongo migration stack:

```shell
# docker stack deploy -c docker-compose-postgres.yml -c docker-compose-mongo.yml -c docker-compose-agent-sql.yml -c docker-compose-agent-agent-mongo.yml migration 
```

* optionally you can run UIs for datasources
```shell
# docker stack deploy -c docker-compose-sql-adminer.yml migration
# docker stack deploy -c docker-compose-kibana.yml migration
# docker stack deploy -c docker-compose-mongo-express.yml migration
```
* To setup apache spark processor engine run following command
```shell
# docker stack deploy -c docker-compose-spark.yml migration
```

### Exposed docker ports
| Port  | Service name                 | Service type |
|-------|------------------------------|--------------|
| 8080  | ctoolkit-agent-sql           |[agent]       |
| 8081  | ctoolkit-agent-elasticsearch |[agent]       |
| 8082  | ctoolkit-agent-mongo         |[agent]       | 
| 5432  | postgres database            |[datasource]  |
| 3306  | mysql database               |[datasource]  |
| 9200  | elasticsearch database       |[datasource]  |
| 27017 | mongo database               |[datasource]  |
| 8180  | sql adminer UI               |[UI]          |
| 8181  | kibana UI                    |[UI]          |
| 8182  | mongo express UI             |[UI]          |
| 8191  | spark dashboard UI master    |[UI]          |
| 8192  | spark dashboard UI worker    |[UI]          |
| 4040  | spark dashboard UI app       |[UI]          |

## SQL setup (postgress)
- DjdbcUrl=jdbc:postgresql://morty:5432/root
- DjdbcUsername=root
- DjdbcPassword=admin123
- DjdbcDriver=org.postgresql.Driver
- DmigrationTargetAgent=ELASTICSEARCH
- DmigrationTargetAgentUrl=http://morty:8081

## Elasticsearch setup
- DelasticsearchHosts=http://morty:9200
- Dmicronaut.server.port=8081

## Mongo setup
- DmongoUri=mongodb://root:admin123@morty:27017
- Dmicronaut.server.port=8082

## Maven build
To run agents locally you need to build it with special maven profile. Each
agent has its own profile:
```shell
# mvn clean install -P sql
# mvn clean install -P elasticsearch
# mvn clean install -P mongo
``` 

## Docker build image
To build docker images run following command:
```shell
# mvn clean install -P sql,elasticsearch,mongo,docker-build-image
```

## Docker push
To push docker images into docker hub run following command:
```shell
# mvn clean install -P sql,elasticsearch,mongo,docker-push
```

## Wiki
To learn more about ctoolkit-agent visit [wiki](https://github.com/turnonline/ctoolkit-agent/wiki)
