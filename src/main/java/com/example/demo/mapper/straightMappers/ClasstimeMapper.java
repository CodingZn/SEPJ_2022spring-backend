package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.Classtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasstimeMapper extends JpaRepository<Classtime, String> {
    Classtime findByName(String name);
}
