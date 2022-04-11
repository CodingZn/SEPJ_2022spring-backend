package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomMapper extends JpaRepository<Classroom, String> {
}
