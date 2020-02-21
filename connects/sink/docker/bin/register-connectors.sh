#!/usr/bin/env bash

## Use the file containing the JSON formated configuration

############# Note:-
## 8083 is the port exposed in dockerfile
## /usr/local/share/kafka/properties/ is the location where all the properties files are stored. See Dockerfile for more details
curl --request POST -sL \
     --url 'http://localhost:8083/connectors'\
     -H "Content-Type: application/json" \
     -- data @/usr/local/share/kafka/properties/distributed/my-custom-sink.json \
     --output './logs/register-connectors-logs.log'