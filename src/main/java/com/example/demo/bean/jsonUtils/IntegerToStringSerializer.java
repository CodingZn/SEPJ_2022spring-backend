package com.example.demo.bean.jsonUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IntegerToStringSerializer  extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer integer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(String.valueOf(integer));
    }
}
