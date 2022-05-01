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
    public Major getABean(String majorid) {
        return majorMapper.findByMajorid(majorid);
    }

    @Override
    public List<Major> getAllBeans() {
        return majorMapper.findAll();
    }

    @Override
    public String createABean(String majorid, Major major) {//只创建，不修改

        Major major1 = getABean(majorid);
        if (major1 == null) {
            major.setMajorid(majorid);
            majorMapper.save(major);
            return "Success";
        }
        else{
            return "Conflict";
        }
    }

    @Override
    public String createBeans(List<Major> beans) {
        beans.removeIf(Objects::isNull);
        for(Major major : beans){
            createABean(major.getMajorid(), major);
        }
        return "Success";
    }

    @Override
    public String changeABean(String majorid, Major major) {//只根据主键修改，不创建
        Major major1 = getABean(majorid);
        if (major1 == null) {
            return "NotFound";
        }
        major.setMajorid(major1.getMajorid());
        majorMapper.save(major);
        return "Success";
    }

    @Override
    public String deleteABean(String majorid) {

        Major major = majorMapper.findByMajorid(majorid);
        if (major != null) {
            majorMapper.delete(major);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<String> majorids = (List<String>) ids;
        for(String majorid : majorids) {
            majorMapper.deleteById(majorid);
        }
        return "Success";
    }
}
