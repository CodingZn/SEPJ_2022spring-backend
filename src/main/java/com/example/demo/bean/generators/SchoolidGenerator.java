package com.example.demo.bean.generators;

import lombok.Setter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

public class SchoolidGenerator extends UUIDGenerator {
    @Setter
    private static int nextSchoolid;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (nextSchoolid > 99 || nextSchoolid < 0){
            throw new HibernateException("院系数量超过最大限制！");
        }
        String generatedId = String.format("%02d", nextSchoolid);
        nextSchoolid = nextSchoolid + 1;
        return generatedId;
    }
}
