FROM confluentinc/cp-kafka-connect:5.2.1

################################################
### A Kafka Connect plugin is:
    #
    #1) an uber JAR containing all of the classfiles for the plugin and its third-party dependencies in a single JAR file; or
    #2) a directory on the file system that contains the JAR files for the plugin and its third-party dependencies.

#### Create a directory "/usr/local/share/kafka/properties" where we will keep all the configuration files
        ## Move all the properties files from your local sytem to "/usr/local/share/kafka/properties"

#### Create a directory "/usr/local/share/kafka/plugins" where we will keep all the kafka connect plugin
        ## Move all the shadow jar from your local sytem to "/usr/local/share/kafka/plugins"
################################################

MAINTAINER "Ajay kumar"
EXPOSE 8083

### Create a directory to store all the properties file used to run kafka-connect eg connect-standalone.properties, connect-distributed.properties, log4j.properties, sink.properties
RUN mkdir -p /usr/local/share/kafka/properties
### copy the contents of your local properties directory in the /usr/local/share/kafka/properties directory of your docker image.
COPY properties /usr/local/share/kafka/properties

### Create a directory to story the connectors or plugin or shadow-jar etc
RUN mkdir -p /usr/local/share/kafka/plugins
#### PROJECT_ARTIFACT_JAR_FILE will get substituted by gradle at build-time with the shadow jar archive name
ARG PROJECT_ARTIFACT_JAR_FILE
COPY $PROJECT_ARTIFACT_JAR_FILE /usr/local/share/kafka/plugins

## Set the env variable CONNECT_PLUGIN_PATH of image- confluentinc/cp-kafka-connect
ENV CONNECT_PLUGIN_PATH='usr/share/java,/usr/local/share/kafka/plugins'

## Copy register-connectors.sh and docker-entrypoint.sh from bin directory to current directory
COPY bin/register-connectors.sh .
RUN chmod +x register-connectors.sh

COPY bin/docker-entrypoint.sh .
RUN chmod +x docker-entrypoint.sh

#####################################
### Add aditional library eg mysql-connector
#####################################
### Add JDBC connector
#COPY lib/mysql-connector-java-5.1.46.jar /usr/share/java/kafka-connect-jdbc/

### Define entrypoint script and start the application
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD [""]