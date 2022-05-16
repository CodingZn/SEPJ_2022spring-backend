package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassarrangeListSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Classarrange> classarranges = (List<Classarrange>) o;
        List<Object> arrangeList = new ArrayList<>();
        for(Classarrange classarrange : classarranges){
            String classtimeid = String.valueOf(classarrange.getClasstime().getClasstimeid());
            String classroomid = String.valueOf(classarrange.getClassroom().getClassroomid());
            Map<String, Object> map = new HashMap<>();
            map.put("classtime", classtimeid);
            map.put("classroom", classroomid);
            map.put("classarrange", classarrange.getId());
            arrangeList.add(map);
        }
        jsonGenerator.writeObject(arrangeList);
    }
}
