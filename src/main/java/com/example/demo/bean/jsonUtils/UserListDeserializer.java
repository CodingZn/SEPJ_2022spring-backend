package com.example.demo.bean.jsonUtils;

import com.alibaba.fastjson.JSON;
import com.example.demo.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListDeserializer extends JsonDeserializer{

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        ArrayNode arrayNode = (ArrayNode) treeNode;
        List<User> teachers = new ArrayList<>();
        for(int i = 0; i < arrayNode.size(); i++){
            String a = arrayNode.get(i).textValue();
            User user = new User();
            user.setUserid(a);
            teachers.add(user);
        }
        return teachers;
    }
}
