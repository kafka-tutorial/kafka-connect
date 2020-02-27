####Kafka Connect Rest API ####
****
The REST API supports application/json as both the request and response entity content type. For example:
<br>
**Accept:** application/json <br>
**Content-Type:** application/json

****
####Kafka Connect Rest API Summery ####

| HTTP   | URI                              | Description                                                                                                   |
|--------|----------------------------------|---------------------------------------------------------------------------------------------------------------|
| GET    | /connectors                      | Gets a list of active connectors.                                                                             |
| POST   | /connectors                      | Creates a new connector, returning the current connector information is successful.                           |
| GET    | /connectors/(string:name)        | Gets information about the connector.                                                                         |
| GET    | /connectors/(string:name)/status | Get current status of the connector, including whether it is running, failed or paused, which worker it is assigned to, error information if it has failed, and the state of all its tasks. |
| GET    | /connectors/(string:name)/config | Gets the configuration for the connector.                                                                     |
| PUT    | /connectors/(string:name)/config | Creates a new connector using the given configuration or updates the configuration for an existing connector. |
| GET    | /connectors/(string:name)/tasks  | Gets a list of tasks current running for the connector.                                                       |
| DELETE | /connectors/(string:name)/       | Deletes a connector, halting all tasks and deleting its configuration.                                        |

****
