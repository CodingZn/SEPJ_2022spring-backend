package com.example.demo.mapper;

import com.example.demo.bean.Classarrange;
import com.example.demo.bean.Classroom;
import com.example.demo.bean.Classtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassarrangeMapper extends JpaRepository <Classarrange, Integer> {
    List<Classarrange> findAllByClassroomAndClasstime(Classroom classroom, Classtime classtime);
}
