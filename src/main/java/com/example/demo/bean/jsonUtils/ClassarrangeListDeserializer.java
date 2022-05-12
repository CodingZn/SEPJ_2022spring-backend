package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;
import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassarrangeListDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        ArrayNode arrayNode = (ArrayNode) treeNode;
        ObjectMapper objectMapper;
        List<Classarrange> classarranges = new ArrayList<>();
        for(int i = 0; i < arrayNode.size(); i++){
            JsonNode arrange = arrayNode.get(i);
            String s = arrange.findValue("classroom").asText();//classroomid
            String t = arrange.findValue("classtime").asText();//classtimeid
            Classroom classroom = new Classroom();
            Classtime classtime = new Classtime();
            classroom.setClassroomid(Integer.parseInt(s));
            classtime.setClasstimeid(Integer.parseInt(t));
            Classarrange classarrange = new Classarrange();
            classarrange.setClassroom(classroom);
            classarrange.setClasstime(classtime);
            classarranges.add(classarrange);// classarrange 中无id，仅有教室和时间信息
        }
        return classarranges;
    }
}
