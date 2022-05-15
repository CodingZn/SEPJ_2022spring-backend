package com.example.demo.mapper;

import com.example.demo.bean.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonMapper extends JpaRepository <Lesson, String> {
    Lesson findByLessonid(int lessonid);
    void deleteByLessonid(int lessonid);
    List<Lesson> findAllByLessoncode(String lessoncode);
    Lesson findByLessonnumberAndSemester(String lessonnumber, String semester);
}
