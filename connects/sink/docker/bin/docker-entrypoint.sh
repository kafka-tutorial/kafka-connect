#!/usr/bin/env bash

if [ -z "$KAFKA_JMX_OPTS" ]; then
#    export KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=your.kafka.broker.hostname -Djava.net.preferIPv4Stack=true"
    export KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
fi

### TODO:-
### Set KAFKA_JMX_HOSTNAME
### Set KAFKA_JMX_PORT
### Set BOOTSTRAP_SERVERS
### Set CONNECT_BOOTSTRAP_SERVERS


# Add external jars to classpath
# Please make sure that the CLASSPATH does not starts with ":/etc/..."
# Because this causes the plugin scanner to scan the entire disk
export CLASSPATH="usr/share/java/kafka/*"
export KAFKA_OPTS="${JAVA_OPTIONS}"
export KAFKA_HEAP_OPTIONS="${JAVA_HEAP_OPTIONS}"

function replaceValueInPropertyFile() {
    ############################
    ### usage: replaceValueInPropertyFile $key $value $filename
    ### usage: replaceValueInPropertyFile 'bootstrap.servers' ${BOOTSTRAP_SERVERS} connect-distributed.properties
    ############################

    local property_name=$1
    local property_value=$2
    local filename=$3
#    sed -i "/property.name=/ s/=.*/=newValue/" yourFile.properties
    sed -i "/${property_name}=/ s/=.*/=${property_value}/" "${filename}"
}

### Replace the value of property "bootstrap.servers" from the one supplied using environment variable
replaceValueInPropertyFile 'bootstrap.servers' "${BOOTSTRAP_SERVERS}" /usr/local/share/kafka/properties/standalone/connect-standalone.properties
replaceValueInPropertyFile 'bootstrap.servers' "${BOOTSTRAP_SERVERS}" /usr/local/share/kafka/properties/distributed/connect-distributed.properties

replaceValueInPropertyFile 'plugin.path' "${CONNECT_PLUGIN_PATH}" /usr/local/share/kafka/properties/standalone/connect-standalone.properties
replaceValueInPropertyFile 'plugin.path' "${CONNECT_PLUGIN_PATH}" /usr/local/share/kafka/properties/distributed/connect-distributed.properties

if [ "${RUN_MODE}" == "distributed" ]; then
    ## Please note that we created properties (/usr/local/share/kafka/properties/distributed/connect-distributed.properties) file in our Dockerfile
    exec connect-distributed /usr/local/share/kafka/properties/distributed/connect-distributed.properties
else
#  PROPERTY_FILES=`find /usr/local/share/kafka/properties/ -type f -maxdepth 1 -name "*-sink.properties" -not -name connect-distributed.properties -not -name log4j.properties`
  PROPERTY_FILES_LOCATION=/usr/local/share/kafka/properties/standalone
  PROPERTY_FILES=`find /usr/local/share/kafka/properties/standalone/ -type f -name "*-sink.properties" -not -name connect-distributed.properties -not -name log4j.properties`

  echo "************ Running Kafka Connect in Standalone mode with config:- ******************"
  cat ${PROPERTY_FILES_LOCATION}/connect-standalone.properties

  exec connect-standalone ${PROPERTY_FILES_LOCATION}/connect-standalone.properties ${PROPERTY_FILES_LOCATION}/my-custom-sink.properties
fi