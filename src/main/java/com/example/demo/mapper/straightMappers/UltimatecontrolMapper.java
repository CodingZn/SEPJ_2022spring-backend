package com.example.demo.mapper.straightMappers;

import com.example.demo.bean.trivialBeans.Ultimatecontrol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UltimatecontrolMapper extends JpaRepository<Ultimatecontrol, String> {
}
