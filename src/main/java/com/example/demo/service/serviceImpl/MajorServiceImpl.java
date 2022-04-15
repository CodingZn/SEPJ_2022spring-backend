package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MajorServiceImpl implements GeneralService<Major> {
    private final MajorMapper majorMapper;

    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    public String getANewId() {//返回一个可用的majornumber(str)
        List<Major> majorList = majorMapper.findAll();
        Major maxmajor;
        if (majorList.stream().max(Comparator.comparing(Major::getMajornumber)).isPresent()){
            maxmajor = majorList.stream().max(Comparator.comparing(Major::getMajornumber)).get();
            System.out.println("getMaxMajornumber=");
            System.out.println(maxmajor.getMajornumber());
            return String.valueOf(maxmajor.getMajornumber() + 1);
        }
        else return "1";



    }

    @Override
    public List<String> getAllIds() {

        List<Major> majorList = majorMapper.findAll();

        List<String> a = majorList.stream().map( u -> String.valueOf(u.getMajornumber())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Major getABean(String majornumber_str) {
        Major major;
        try {
            int majornumber = Integer.parseInt(majornumber_str);
            major = majorMapper.findByMajornumber(majornumber);
            return major;
        } catch (NumberFormatException e) {//传入majornumber格式不对
            return null;
        }
    }

    @Override
    public List<Major> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String majornumber_str, Major major) {//只创建，不修改

        Major major1 = getABean(majornumber_str);
        if (major1 == null) {
            try{
                major.setMajornumber(Integer.parseInt(majornumber_str));
            }
            catch (Exception e){
                return "FormError";
            }
            if (major.getName().equals("") || major.getSchool().equals(""))  return "FormError";
            majorMapper.save(major);
            return "Success";

        }
        else{
            return "Conflict";
        }


    }

    @Override
    public String changeABean(String majornumber_str, Major major) {//只根据主键修改，不创建
        Major major1 = getABean(majornumber_str);
        if (major1 == null) {
            return "NotFound";
        }
        if (major.getName().equals("") || major.getSchool().equals(""))
            return "FormError";

        major.setMajornumber(major1.getMajornumber());
        majorMapper.save(major);
        return "Success";
    }

    @Override
    public String deleteABean(String majornumber_str) {

        int majornumber;
        try {
            majornumber = Integer.parseInt(majornumber_str);
        } catch (NumberFormatException e) {//传入majornumber格式不对
            return "NotFound";
        }
        Major major = majorMapper.findByMajornumber(majornumber);
        if (major != null) {
            majorMapper.delete(major);
            return "Success";
        } else {
            return "NotFound";
        }
    }
}
