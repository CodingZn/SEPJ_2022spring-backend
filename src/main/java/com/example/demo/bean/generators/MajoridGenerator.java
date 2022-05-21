package com.example.demo.bean.generators;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import lombok.Setter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MajoridGenerator extends UUIDGenerator {

    @Setter
    private static int nextMajorid;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (nextMajorid > 999 || nextMajorid < 0){
            throw new HibernateException("专业数量超过最大限制！");
        }
        if (nextMajorid == 0){
            nextMajorid = nextMajorid + 1;
            return "all";
        }
        String generatedId = String.format("%03d", nextMajorid);
        nextMajorid = nextMajorid + 1;
        return generatedId;
    }
}
