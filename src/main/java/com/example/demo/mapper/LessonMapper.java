package com.example.demo.mapper;

import com.example.demo.bean.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonMapper extends JpaRepository <Lesson, String> {
    Lesson findByLessonid(int lessonid);
    List<Lesson> findAllByTeacher(String teachername);
    List<Lesson> findAllByStatus(String status);
    List<Lesson> findAllByTeacherOrStatus(String teachername, String status);
}
