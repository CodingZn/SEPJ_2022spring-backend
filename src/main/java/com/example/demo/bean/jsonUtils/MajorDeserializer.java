package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Major;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

public class MajorDeserializer extends BasicDeserializer<Major> {
    @Override
    Major getBean(String id) {
        Major bean = new Major();
        bean.setMajorid(id);
        return bean;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return super.deserialize(jsonParser, deserializationContext);
    }
}
