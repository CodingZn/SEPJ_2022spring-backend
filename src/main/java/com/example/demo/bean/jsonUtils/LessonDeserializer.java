package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Lesson;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LessonDeserializer extends BasicDeserializer<Lesson> {

    @Override
    Lesson getBean(String id) {
        Lesson bean = new Lesson();
        bean.setLessonid(Integer.parseInt(id));
        return bean;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return super.deserialize(jsonParser, deserializationContext);
    }
}
