package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MajorServiceImpl implements GeneralService<Major> {
    private final MajorMapper majorMapper;

    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    @Override
    public List<String> getAllIds() {
        List<Major> majorList = majorMapper.findAll();
        return majorList.stream().map(Major::getMajorid).toList();
    }

    @Override
    public Major getABean(String id) {
        return majorMapper.findByMajorid(id);
    }

    @Override
    public List<Major> getAllBeans() {
        return majorMapper.findAll();
    }

    @Override
    public String createABean(String id, Major bean) {//只创建，不修改

        Major bean1 = getABean(id);
        if (bean1 == null) {
            bean.setMajorid(id);
            majorMapper.save(bean);
            return "Success";
        }
        else{
            return "Conflict";
        }
    }

    @Override
    public String createBeans(List<Major> beans) {
        beans.removeIf(Objects::isNull);
        for(Major bean : beans){
            createABean(bean.getMajorid(), bean);
        }
        return "Success";
    }

    @Override
    public String changeABean(String id, Major bean) {//只根据主键修改，不创建
        Major bean1 = getABean(id);
        if (bean1 == null) {
            return "NotFound";
        }
        bean.setMajorid(bean1.getMajorid());
        majorMapper.save(bean);
        return "Success";
    }

    @Override
    public String deleteABean(String id) {

        Major bean = majorMapper.findByMajorid(id);
        if (bean != null) {
            majorMapper.delete(bean);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<String> idList = (List<String>) ids;
        for(String id : idList) {
            majorMapper.deleteById(id);
        }
        return "Success";
    }
}
