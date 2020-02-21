package com.example.sink.config.provider;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.kafka.common.config.ConfigData;
import org.apache.kafka.common.config.provider.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * to create a connector, the only available mean is through REST calls, this means I need to send my information through the wire, unprotected. In order to secure this, kafka introduced the new ConfigProvider seen here. This is helpful as it allows to set properties in the server and then reference them in the rest call, like so:
 * <p>
 * {
 * .
 * "property":"${file:/path/to/file:nameOfThePropertyInFile}"
 * .
 * }
 * This works really well, just by adding the property file on the server and adding the following config on the distributed.properties file:
 * <p>
 * config.providers=file   # multiple comma-separated provider types can be specified here
 * config.providers.file.class=org.apache.kafka.common.config.provider.FileConfigProvider
 * While this solution works, it really does not help to easy my concerns regarding security, as the information now passed from being sent over the wire, to now be seating on a repository, with text on plain sight for everyone to see. The kafka team foresaw this issue and allowed clients to produce their own configuration providers implementing the interface ConfigProvider. I have created my own implementation and packaged in a jar, givin it the sugested final name:
 * <p>
 * META-INF/services/org.apache.kafka.common.config.ConfigProvider
 * and added the following entry in the distributed file:
 * <p>
 * config.providers=cust
 * config.providers.cust.class=com.somename.configproviders.CustConfigProvider
 */
public class EnvironmentConfigProvider implements ConfigProvider {
    // Learn how to implement ConfigProvider from:- https://github.com/giogt/kafka-env-config-provider
    // Refer:- https://stackoverflow.com/questions/57995742/creating-and-using-a-custom-kafka-connect-configuration-provider

    //Refer for sample code:-
    //  1):-https://github.com/giogt/kafka-env-config-provider

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfigProvider.class);

    @Override
    public void configure(Map<String, ?> configs) {
        //TODO:- Set whitelisted prefix
        //TODO:- Remove below logger as it may contains password
        LOGGER.info("Received properties:- {}", configs);
    }


    /**
     * Retrieves the data from the Environment variable.
     *
     * @param path:- When the data needs to be fetched from environment variable, the path can be think of environment.
     *               Take other eg when data resides in any properties file then in that case the path can be the location of file where the data resides.
     * @return the configuration data:- i.e load all the data from given path (here it is environment) and return the configuration data.
     */

    @Override
    public ConfigData get(String path) {
        LOGGER.debug("get(path) method invoked [path={}] ...", path);
        Map<String, String> envData = Collections.unmodifiableMap(System.getenv());
        ConfigData configData = new ConfigData(new ConfigMap(envData));
        LOGGER.info("returning ConfigData [data={}, ttl={}]", configData.data(), configData.ttl());
        return configData;
    }

    @Override
    public ConfigData get(String path, Set<String> keys) {
        if (null == keys || keys.isEmpty()) {
            return new ConfigData(new HashMap<>());
        }
        Map<String, String> envData = System.getenv()
                .entrySet()
                .stream()
                .filter(it -> keys.contains(it.getKey()))
                .collect(Collectors.toMap(it -> it.getKey(), it -> it.getValue()));
        return new ConfigData(new ConfigMap(envData));
    }

    @Override
    public void close() throws IOException {

    }

    private static class ConfigMap extends HashMap<String, String> {
        StringSubstitutor substitutor;

        public ConfigMap(Map<String, String> another) {
            super(another);
            substitutor = new StringSubstitutor(another);
//            substitutor = new StringSubstitutor(another, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE, DEFAULT_VAR_DEFAULT);
        }

        @Override
        public String get(Object key) {
            return substitutor.replace(key);
        }

        @Override
        public boolean containsKey(Object key) {
            return StringUtils.isNotBlank(substitutor.replace(key));
        }

    }
}
