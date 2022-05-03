package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UserDeserializer extends BasicDeserializer<User> {

    @Override
    User getBean(String id) {
        User bean = new User();
        bean.setUserid(id);
        return bean;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return super.deserialize(jsonParser, deserializationContext);
    }
}
