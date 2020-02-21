package com.example.sink.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.storage.Converter;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.sink.util.SchemaUtil.OPTIONAL_STRING_;
import static com.example.sink.util.SchemaUtil.STRING_;

public class UserConverter implements Converter {

    private static final String ADDRESS_SCHEMA_NAME = "address";
    private static final String USER_SCHEMA_NAME = "user";

    private static final String ADDRESS_LINE_1 = "addressLine1";
    private static final String ADDRESS_LINE_2 = "addressLine2";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String PIN_CODE = "pincode";
    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL_ID = "emailId";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String ADDRESSES = "addresses";

    private static final Schema ADDRESS_SCHEMA = SchemaBuilder.struct()
            .name(ADDRESS_SCHEMA_NAME)
            .field(ID, STRING_)
            .field(ADDRESS_LINE_1, STRING_)
            .field(ADDRESS_LINE_2, OPTIONAL_STRING_)
            .field(CITY, STRING_)
            .field(STATE, STRING_)
            .field(COUNTRY, STRING_)
            .field(PIN_CODE, STRING_)
            .build();

    //See:- https://github.com/axbaretto/kafka/blob/master/connect/api/src/test/java/org/apache/kafka/connect/data/StructTest.java
    private static final Schema USER_SCHEMA = SchemaBuilder.struct()
            .name(USER_SCHEMA_NAME)
            .doc("The Schema for User")
            .field(ID, STRING_)
            .field(FIRST_NAME, OPTIONAL_STRING_)
            .field(LAST_NAME, OPTIONAL_STRING_)
            .field(EMAIL_ID, STRING_)
            .field(DATE_OF_BIRTH, STRING_)
            .field(ADDRESSES, SchemaBuilder.array(ADDRESS_SCHEMA).build())
            .build();
    ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        return new byte[0];
    }

    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        return Optional.ofNullable(value)
                .map(readAsMap())
                .map(createUserStruct())
                .map(it -> new SchemaAndValue(USER_SCHEMA, it))
                .orElse(new SchemaAndValue(USER_SCHEMA, null));
    }

    private Function<Map<String, ?>, Struct> createUserStruct() {
        return it -> new Struct(USER_SCHEMA)
                .put(ADDRESS_LINE_1, it.get(ADDRESS_LINE_1))
                .put(ADDRESS_LINE_2, it.get(ADDRESS_LINE_2))
                .put(CITY, it.get(CITY))
                .put(STATE, it.get(STATE))
                .put(COUNTRY, it.get(COUNTRY))
                .put(PIN_CODE, it.get(PIN_CODE))
                .put(ID, it.get(ID))
                .put(FIRST_NAME, it.get(FIRST_NAME))
                .put(LAST_NAME, it.get(LAST_NAME))
                .put(EMAIL_ID, it.get(EMAIL_ID))
                .put(ADDRESSES, createAddressStruct(it));
    }

    private List<Struct> createAddressStruct(Map<String, ?> addresses) {
        return Optional.ofNullable(addresses.get(ADDRESSES))
                .map(it -> (List<Map<String, String>>) it)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(it -> new Struct(USER_SCHEMA.field(ADDRESSES).schema().valueSchema())
                        .put(ID, it.get(ID))
                        .put(ADDRESS_LINE_1, it.get(ADDRESS_LINE_1))
                        .put(ADDRESS_LINE_2, it.get(ADDRESS_LINE_2))
                        .put(CITY, it.get(CITY))
                        .put(STATE, it.get(STATE))
                        .put(COUNTRY, it.get(COUNTRY))
                        .put(PIN_CODE, it.get(PIN_CODE)))
                .collect(Collectors.toList());
    }

    private Function<byte[], Map<String, Object>> readAsMap() {
        return it -> {
            Map<String, Object> data = new HashMap<>();
            try {
                data = objectMapper.readValue(it, HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        };
    }
}
