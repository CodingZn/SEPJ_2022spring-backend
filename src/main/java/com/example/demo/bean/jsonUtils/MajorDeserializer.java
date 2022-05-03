package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.School;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MajorDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String schoolid = jsonParser.getText();
        School school = new School();
        school.setSchoolid(schoolid);
        return school;
    }
}
