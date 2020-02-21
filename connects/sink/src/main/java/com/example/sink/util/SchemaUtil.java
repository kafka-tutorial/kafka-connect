package com.example.sink.util;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Time;

public class SchemaUtil {
    //See:- https://github.com/axbaretto/kafka/blob/master/connect/api/src/test/java/org/apache/kafka/connect/data/StructTest.java

    public static final Schema SHORT_ = Schema.INT16_SCHEMA;
    public static final Schema INTEGER_ = Schema.INT32_SCHEMA;
    public static final Schema LONG_ = Schema.INT64_SCHEMA;
    public static final Schema FLOAT = Schema.FLOAT32_SCHEMA;
    public static final Schema DOUBLE = Schema.FLOAT64_SCHEMA;
    public static final Schema BOOLEAN_ = Schema.BOOLEAN_SCHEMA;
    public static final Schema BYTE_ = Schema.BYTES_SCHEMA;
    public static final Schema STRING_ = Schema.STRING_SCHEMA;
    public static final Schema TIME_AS_LONG_ = Time.builder().schema();

    public static final Schema OPTIONAL_SHORT_ = Schema.OPTIONAL_INT16_SCHEMA;
    public static final Schema OPTIONAL_INTEGER_ = Schema.OPTIONAL_INT32_SCHEMA;
    public static final Schema OPTIONAL_LONG_ = Schema.OPTIONAL_INT64_SCHEMA;
    public static final Schema OPTIONAL_FLOAT = Schema.OPTIONAL_FLOAT32_SCHEMA;
    public static final Schema OPTIONAL_DOUBLE = Schema.OPTIONAL_FLOAT64_SCHEMA;
    public static final Schema OPTIONAL_BOOLEAN_ = Schema.OPTIONAL_BOOLEAN_SCHEMA;
    public static final Schema OPTIONAL_BYTE_ = Schema.OPTIONAL_BYTES_SCHEMA;
    public static final Schema OPTIONAL_STRING_ = Schema.OPTIONAL_STRING_SCHEMA;
    public static final Schema OPTIONAL_TIME_AS_LONG_ = Time.builder().optional().schema();
}
