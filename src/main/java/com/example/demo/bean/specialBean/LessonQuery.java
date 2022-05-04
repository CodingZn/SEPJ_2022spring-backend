package com.example.demo.bean.specialBean;

import com.example.demo.bean.Lesson;
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

    private int mode;

    private String semester;

    private String classroom_name;

    private String classtime_name;

    private String fuzzyLessonCode;

    private String fuzzyLessonName;

    private String fuzzyLessonTeacherName;

    private List<Lesson> result;

    private boolean is_processed = false;

}
