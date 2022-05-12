package com.example.demo.bean.specialBean;

import com.example.demo.bean.Lesson;
import com.example.demo.bean.jsonUtils.LessonListSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonQuery {

    private String semester;

    private String classroom_name;

    private String classtime_name;

    private String fuzzyLessonCode;

    private String fuzzyLessonName;

    private String fuzzyLessonTeacherName;

    @JsonSerialize(using = LessonListSerializer.class)
    private List<Lesson> result;

}
