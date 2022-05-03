package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;

import java.io.IOException;

public class UserListDeserializer extends JsonDeserializer{

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String a = jsonParser.getValueAsString();
        System.out.println(a);
        return a;
    }
}
