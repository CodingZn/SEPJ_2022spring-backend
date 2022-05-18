package com.example.demo.bean.jsonUtils;

import com.example.demo.bean.Lesson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class LessonListSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<Lesson> lessons = (List<Lesson>) o;
//        List<String> lessonids = lessons.stream().map(lesson -> String.valueOf(lesson.getLessonid())).toList();
        List<Lesson> lessonList = lessons.stream().map(lesson -> {
            lesson.setClassarrange(null);
            return lesson;
        }).toList();
        jsonGenerator.writeObject(lessonList);
    }
}
