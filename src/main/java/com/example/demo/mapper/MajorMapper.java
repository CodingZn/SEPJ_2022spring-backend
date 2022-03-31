package com.example.demo.mapper;

import com.example.demo.bean.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorMapper extends JpaRepository<Major, String> {
    public Major findByMajornumber(int majornumber);

}
