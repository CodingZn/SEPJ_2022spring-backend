package com.example.demo.mapper;

import com.example.demo.bean.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MajorMapper extends JpaRepository<Major, String> {
    Major findByMajornumber(int majornumber);
    Major findBySchool(String school);
    Major findByName(String name);
}
