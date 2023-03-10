package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Major;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MajorSerializer extends BasicSerializer<Major> {
    @Override
    Object getId(Major bean) {
        return bean.getMajorid();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        super.serialize(o, jsonGenerator, serializerProvider);
    }
}
