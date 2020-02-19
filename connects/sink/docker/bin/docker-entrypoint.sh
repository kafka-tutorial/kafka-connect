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

### Replace the value of property "bootstrap.servers" from the one supplied using environment variable
sed "s/bootstrap.servers=.*/bootstrap.servers=${BOOTSTRAP_SERVERS}" /usr/local/share/kafka/properties/connect-standalone.properties
sed "s/bootstrap.servers=.*/bootstrap.servers=${BOOTSTRAP_SERVERS}" /usr/local/share/kafka/properties/connect-distributed.properties

if [ "${RUN_MODE}" == "distributed" ]; then
    ## Please note that we created properties (/usr/local/share/kafka/properties/connect-distributed.properties) file in our Dockerfile
    exec connect-distributed /usr/local/share/kafka/properties/connect-distributed.properties
else
#  PROPERTY_FILES=`find /usr/local/share/kafka/properties/ -type f -maxdepth 1 -name "*-sink.properties" -not -name connect-distributed.properties -not -name log4j.properties`
  PROPERTY_FILES=`find /usr/local/share/kafka/properties/ -type f -name "*-sink.properties" -not -name connect-distributed.properties -not -name log4j.properties`
  exec connect-standalone /usr/local/share/kafka/properties/connect-distributed.properties my-custom-sink.properties
fi