package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<String> a = majorList.stream().map( u -> String.valueOf(u.getMajorid())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Major getABean(String majornumber) {
        Major major;
        major = majorMapper.findByMajornumber(majornumber);
        return major;

    }

    @Override
    public List<Major> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String majornumber, Major major) {//只创建，不修改

        Major major1 = getABean(majornumber);
        if (major1 == null) {

            major.setMajorid(majornumber);

            if (major.getName().equals("") || major.getSchool().equals(""))  return "FormError";
            majorMapper.save(major);
            return "Success";

        }
        else{
            return "Conflict";
        }


    }

    @Override
    public String changeABean(String majornumber, Major major) {//只根据主键修改，不创建
        Major major1 = getABean(majornumber);
        if (major1 == null) {
            return "NotFound";
        }
        if (major.getName().equals("") || major.getSchool().equals(""))
            return "FormError";

        major.setMajorid(major1.getMajorid());
        majorMapper.save(major);
        return "Success";
    }

    @Override
    public String deleteABean(String majornumber) {


        Major major = majorMapper.findByMajornumber(majornumber);
        if (major != null) {
            majorMapper.delete(major);
            return "Success";
        } else {
            return "NotFound";
        }
    }
}
