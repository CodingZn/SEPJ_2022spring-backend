package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Lessonrequest;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LessonrequestSerializer extends BasicSerializer<Lessonrequest> {
    @Override
    Object getId(Lessonrequest bean) {
        return bean.getLessonrequestid();
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        super.serialize(o, jsonGenerator, serializerProvider);
    }
}
