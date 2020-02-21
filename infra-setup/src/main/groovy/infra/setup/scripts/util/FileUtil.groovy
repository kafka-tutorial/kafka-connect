@groovy.lang.Grapes(value = [
        @groovy.lang.Grab(group = 'com.opencsv', module = 'opencsv', version = '5.0')
])
import com.opencsv.CSVReader
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.SourceURI

import java.nio.file.Path
import java.nio.file.Paths

@SourceURI
URI sourceUri

Path scriptLocation = Paths.get(sourceUri)
String scriptFileName = "${scriptLocation.getFileName()}"

println("Current scriptLocation:- ${scriptLocation}")
println("Current scriptFileName:- ${scriptFileName}")

List<Map> parseCsvToMap(String fileName) {
    def reader = new CSVReader(new FileReader(new File(fileName)))
    def output = reader.collect { it[0].split(';') }.with { rows ->
        def header = rows.head()
        def dataRows = rows.tail()

        dataRows.collect { row ->
            [header, row].transpose().collectEntries()
        }
    }
}

/**
 * See:- https://groovy-lang.org/json.html
 * Example:-
 *
 def object = jsonSlurper.parseText('{ "myList": [4, 8, 15, 16, 23, 42] }')
 assert object instanceof Map
 assert object.myList instanceof List
 assert object.myList == [4, 8, 15, 16, 23, 42]

 * @param jsonString to parse to Map/List
 */
def parseFromJsonString(String jsonString) {
    def jsonSlurper = new JsonSlurper()
    return jsonSlurper.parseText(jsonString)
}

//See:- https://groovy-lang.org/json.html
String parseToJsonString(Object dataToParse, boolean prettyPrint = false) {
    if (prettyPrint) {
        return JsonOutput.prettyPrint(dataToParse)
    }
    JsonOutput.toJson(dataToParse)
}