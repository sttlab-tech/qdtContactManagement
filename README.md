# qdtContactManagement

This package implements a Contact Management API with a contract-first approach.  

The Open API Specification ContactManagementAPI.yml is located under ./resources/api  
A Postman collection ContactManagement.postman_collection.json is provided under ./resources/tests  

##  Deployment

### Prerequisites

Ensure you have a Postgres DB to connect to.  
Create the two tables using the DDL files provided unser ./resources/database 

### Traditional deployment in an Integration Server (IS)

Connect to the IS / MSR admin console and configure the JDBC adapter with the following properties, in order to connect it to the Postgres db:
-   Connection Type: webMethods Adapter for JDBC Connection
-   Package Name: qdtContactManagement
-   Transaction Type: LOCAL_TRANSACTION
-   Driver Group: Default
-   DataSource Class: org.postgresql.ds.PGSimpleDataSource
-   Server Name: server name of your database server
-   User: user to connect to your database server
-   Password: password to connect to your database server
-   Database Name: name of the database
-   Port Number: port to connect to the database
Activate the adapter.

Place yourself in the packages folder of your IS and clone the Github repos:

```
git clone https://github.com/staillansag/qdtFramework.git
git clone https://github.com/staillansag/qdtContactManagement.git
```

Restart the IS / MSR, then connect to the admin console and check the packages are active.
  

### Containerized deployment in a Microservice Runtime (MSR)

####    Build of the MSR base image

This is documented in this Github repo: https://github.com/staillansag/qdtBase  
We create a custom base image on top of the MSR product image, which includes:
-   the WmJDBCAdapter package
-   the Postgres JDBC driver


####    Build of the microservice image

We need build a new MSR image that comprises our API impleemntation package and its dependencies (our framework package.)  
As part of the build you need to fetch a package using


```
# This references the custom base image I have built - this image is available at dockerhub with public access
FROM staillansag/webmethods-microservicesruntime:10.15.0.9-qdt

# wpm needs a Github token to fetch the qdtFramework, we receive it as a build argument and store it in an environment variable
ARG GIT_TOKEN
ENV GIT_TOKEN=$GIT_TOKEN

# Our repo contains the qdtContactManagement package, so we copy its content to the image
ADD --chown=sagadmin . /opt/softwareag/IntegrationServer/packages/qdtContactManagement

# We also need to add another repo, which contains our framework. We use the webMethods package manager (wpm) to do so
RUN /opt/softwareag/wpm/bin/wpm.sh install -u staillansag -p $GIT_TOKEN -r https://github.com/staillansag -d /opt/softwareag/IntegrationServer qdtFramework
```
  
To perform the build, use the following command:
```
docker build --build-arg GIT_TOKEN=<github-token> -t <your-image-name> .
```

You can use this Github PAT token, which only grants read access to the qdtFramework repo: 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  


####    Deployment in a simple Docker host using docker-compose

A docker-compose.yml file is available under ./resources/docker-compose, which creates a full stack environment to run the microservice with:
-   A Postgres container
-   A Universal Messaging container with a single realm
-   A Microservice runtime container that hosts our microservice package and its dependencies  

The MSR works together with an application.properties file that is injected into the container when it is created.  
This properties file contains the information items required to connect to the database and the Universal Messaging realm. It also contains some configuration elements like the Administrator user's password.  
Some property values reference environment variables using the $env{ENV_VARIABLE_NAME} notation. To inject these values we use an .env file that is located next to the docker-compose.yml file. The .env file provided in the repo can be used "as is", but it goes without saying that this is only for discovery and testing.  

You also need to provide license files for the MSR and the UM. These need to be placed inside ./resources/docker-compose/license/msr-license.xml and ./resources/docker-compose/license/msr-license.xml files.

To run the docker compose stack, place yourself under ./resources/docker-compose and execute the following command: `docker-compose up -d` (in some environments it's going to be `docker compose up -d`)  
To check the logs of each container, use:
-   `docker logs msr` for the MSR
-   `docker logs umserver` for the UM realm
-   `docker logs postgresql` for Postgres
To stop the containers, use the following command: `docker-compose down` (in some environments it's going to be `docker compose down`) 
