package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Lesson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LessonSerializer extends BasicSerializer<Lesson> {

    @Override
    Object getId(Lesson bean) {
        return bean.getLessonid();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        super.serialize(o, jsonGenerator, serializerProvider);
    }
}
