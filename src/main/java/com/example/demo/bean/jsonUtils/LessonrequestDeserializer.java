package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Lessonrequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class LessonrequestDeserializer extends BasicDeserializer<Lessonrequest> {
    @Override
    Lessonrequest getBean(String id) {
        Lessonrequest bean = new Lessonrequest();
        bean.setLessonrequestid(Integer.parseInt(id));
        return bean;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return super.deserialize(jsonParser, deserializationContext);
    }
}
