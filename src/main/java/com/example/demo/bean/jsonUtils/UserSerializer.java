package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserSerializer extends BasicSerializer<User> {

    @Override
    Object getId(User bean) {
        return bean.getUserid();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        super.serialize(o, jsonGenerator, serializerProvider);
    }
}
