package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Classtime;
import com.example.demo.mapper.ClasstimeMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClasstimeServiceImpl implements GeneralService<Classtime> {
    private final ClasstimeMapper classtimeMapper;

    @Autowired
    public ClasstimeServiceImpl(ClasstimeMapper classtimeMapper) {
        this.classtimeMapper = classtimeMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<Classtime> beans = classtimeMapper.findAll();
        return beans.stream().map(u -> String.valueOf(u.getClasstimeid())).toList();
    }

    @Override
    public Classtime getABean(String id) {
        return classtimeMapper.findByClasstimeid(Integer.parseInt(id));
    }

    @Override
    public List<Classtime> getAllBeans() {
        return classtimeMapper.findAll();
    }

    @Override
    public String createABean(Classtime bean) {
        classtimeMapper.save(bean);
        return "Success";
    }

    @Override
    public String createBeans(List<Classtime> beans) {
        beans.removeIf(Objects::isNull);
        for(Classtime bean : beans){
            createABean(bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, Classtime bean) {
        Classtime bean1 = getABean(id);
        if (bean1 == null)
            return "NotFound";
        else{
            bean.setClasstimeid(Integer.parseInt(id));

            classtimeMapper.save(bean);
            return "Success";
        }
    }

    private String deleteABean(int id){
        Classtime bean1 = classtimeMapper.findByClasstimeid(id);
        if (bean1 != null) {
            classtimeMapper.delete(bean1);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteABean(String id) {
        int id_int = Integer.parseInt(id);
        return deleteABean(id_int);
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<Integer> beanids = (List<Integer>) ids;
        for(Integer beanid : beanids) {
            classtimeMapper.deleteByClasstimeid(beanid);
        }
        return "Success";
    }
}
