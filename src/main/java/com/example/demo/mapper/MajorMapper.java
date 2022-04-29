package com.example.demo.mapper;

import com.example.demo.bean.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MajorMapper extends JpaRepository<Major, String> {
    Major findByMajornumber(String majornumber);
    Major findBySchool(String school);//gai
    Major findByName(String name);
    Major findFirstBySchool(String school);
    Major findFirstByName(String name);
}
