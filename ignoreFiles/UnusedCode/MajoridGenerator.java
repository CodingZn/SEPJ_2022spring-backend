package com.example.demo.bean.generators;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MajoridGenerator extends UUIDGenerator {


    @Autowired
    private final MajorMapper majorMapper;

    public MajoridGenerator(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        List<Major> majorList = majorMapper.findAll();
        Optional<Major> maxmajor = majorList.stream().max(Comparator.comparing(u-> Integer.parseInt(u.getMajorid())));
        if (maxmajor.isPresent()){
            int maxmajorid = Integer.parseInt(maxmajor.get().getMajorid());
            if (maxmajorid > 999)
                throw new HibernateException("can't have more Major");
            return String.format("%3d", maxmajorid + 1);
        }
        else{
            return "001";
        }
    }
}
