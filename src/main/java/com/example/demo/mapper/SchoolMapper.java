package com.example.demo.mapper;

import com.example.demo.bean.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SchoolMapper extends JpaRepository <School, String> {
    School findBySchoolid(String schoolid);
}
