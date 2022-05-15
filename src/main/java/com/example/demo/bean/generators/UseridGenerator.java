package com.example.demo.bean.generators;

import com.example.demo.bean.User;
import lombok.Setter;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.util.*;

public class UseridGenerator extends UUIDGenerator {
    @Setter
    private static int nextStudentid;
    @Setter
    private static int nextTeacherid;
    @Setter
    private static String now_year;

    private String entityName;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        entityName = params.getProperty(ENTITY_NAME);
        if (entityName==null) {
            throw new MappingException("no entity name");
        }
        super.configure(type, params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        User user = (User) object;
        if (nextStudentid > 9999 || nextStudentid < 0)
            throw new HibernateException("同年级学生数量超过最大限制！");
        if (nextTeacherid > 999999 || nextTeacherid < 0)
            throw new HibernateException("教师数量超过最大限制！");
        if (user.getUsertype() == User.Type.student){
            String id = now_year + String.format("%04d",nextStudentid);
            nextStudentid = nextStudentid + 1;
            return id;
        }
        else if (user.getUsertype() == User.Type.teacher) {
            String id = now_year + String.format("%06d",nextTeacherid);
            nextTeacherid = nextTeacherid + 1;
            return id;
        }
        else {
            return user.getUserid();
        }

    }
}
