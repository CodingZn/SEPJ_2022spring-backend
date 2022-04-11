package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.trivialBeans.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomMapper extends JpaRepository<Classroom, String> {
    Classroom findByNameAndStatus(String name, String status);
}
