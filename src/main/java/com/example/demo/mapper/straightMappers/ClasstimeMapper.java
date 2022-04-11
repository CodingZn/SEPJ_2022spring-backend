package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.trivialBeans.Classtime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasstimeMapper extends JpaRepository<Classtime, String> {
}
