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
        if (user.getUsertype() == User.Type.student){
            return now_year + String.format("%04d",nextStudentid);
        }
        else if (user.getUsertype() == User.Type.teacher) {
            return now_year + String.format("%06d",nextTeacherid);
        }
        else {
            throw new HibernateException("不能创建管理员！");
        }

    }
}
