package com.example.demo.bean.jsonUtils;


import com.example.demo.bean.School;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SchoolSerializer extends BasicSerializer<School> {


    @Override
    Object getId(School bean) {
        return bean.getSchoolid();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        super.serialize(o, jsonGenerator, serializerProvider);
    }
}
