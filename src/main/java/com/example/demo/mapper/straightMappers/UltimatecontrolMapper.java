package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.trivialBeans.Ultimatectrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimatecontrolMapper extends JpaRepository<Ultimatectrl, String> {
    Ultimatectrl findByName(String name);
}
