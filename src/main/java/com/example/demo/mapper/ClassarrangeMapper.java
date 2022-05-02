package com.example.demo.mapper;

import com.example.demo.bean.Classarrange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassarrangeMapper extends JpaRepository <Classarrange, Integer> {
}
