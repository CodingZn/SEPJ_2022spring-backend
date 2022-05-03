package com.example.demo.bean.jsonUtils;


import com.example.demo.bean.School;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MajorSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        School school = (School) o;
        if (school == null){
            jsonGenerator.writeNull();
        }
        else if (school.getSchoolid() == null){
            jsonGenerator.writeNull();
        }
        else jsonGenerator.writeString(school.getSchoolid());
    }
}
