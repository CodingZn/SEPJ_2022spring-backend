package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Major;
import com.example.demo.bean.School;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SchoolDeserializer extends BasicDeserializer<School> {

    @Override
    School getBean(String id) {
        School bean = new School();
        bean.setSchoolid(id);
        return bean;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return super.deserialize(jsonParser, deserializationContext);
    }
}
