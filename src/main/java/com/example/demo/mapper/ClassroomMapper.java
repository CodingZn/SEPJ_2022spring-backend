package com.example.demo.mapper;

import com.example.demo.bean.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomMapper extends JpaRepository<Classroom, String> {
    Classroom findByClassroomid(int classroomid);
}
