package com.example.demo.mapper;

import com.example.demo.bean.Major;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MajorMapper extends JpaRepository<Major, String> {
    public Major findByMajornumber(int majornumber);

}
