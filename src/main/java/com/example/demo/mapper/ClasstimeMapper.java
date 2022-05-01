package com.example.demo.mapper;

import com.example.demo.bean.Classtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasstimeMapper extends JpaRepository<Classtime, String> {
    Classtime findByClasstimeid(int classtimeid);
    void deleteByClasstimeid(int classtimeid);
}
