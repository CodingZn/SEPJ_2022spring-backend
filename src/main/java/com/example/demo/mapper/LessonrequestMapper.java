package com.example.demo.mapper;

import com.example.demo.bean.Lessonrequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonrequestMapper extends JpaRepository<Lessonrequest, String> {
    Lessonrequest findByLessonrequestid(int lessonrequestid);
    void deleteByLessonrequestid(int lessonrequestid);
}
