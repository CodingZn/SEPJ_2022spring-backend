package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class ClassarrangeListSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Classarrange> users = (List<Classarrange>) o;
        List<String> userids = users.stream().map(u->String.valueOf(u.getId())).toList();
        jsonGenerator.writeObject(userids);
    }
}
