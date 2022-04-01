package com.example.demo.service.serviceImpl;

import com.example.demo.bean.Major;
import com.example.demo.mapper.MajorMapper;
import com.example.demo.service.UserService;
import com.example.demo.bean.UserBean;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final MajorMapper majorMapper;

    //将DAO(Mapper)层注入Service层
    @Autowired
    public UserServiceImpl(UserMapper userMapper, MajorMapper majorMapper) {
        this.userMapper = userMapper;
        this.majorMapper = majorMapper;
    }


    @Override
    public UserBean login(String schoolnumber, String password) {
        return userMapper.findBySchoolnumberAndPassword(schoolnumber, password);
    }

    @Override
    public String register(UserBean userBean) {
        String schoolnumber, newidentitynumber;
        UserBean a, b;
        schoolnumber = userBean.getSchoolnumber();
        newidentitynumber = userBean.getIdentitynumber();
        a = userMapper.findBySchoolnumber(schoolnumber);
        b = userMapper.findByIdentitynumber(newidentitynumber);
        if (a != null)
            return "SchoolnumberConflict";
        else if (b != null)
            return "IdentitynumberConflict";
        else {
            userBean.setPassword("fDu666666");//统一设置初始密码
            if (userBean.verifyform()) {
                userMapper.save(userBean);
                return "Success";
            } else
                return "FormError";

        }


    }


    @Override
    public String changePassword(String schoolnumber, String newpassword) {
        UserBean userBean;
        userBean = userMapper.findBySchoolnumber(schoolnumber);

        if (Objects.equals(newpassword, "fDu666666")) {//新密码为初始密码
            return "NotAllowedPassword";//新密码错误
        } else if (Objects.equals(userBean.getPassword(), "fDu666666")) {//该用户当前密码为初始密码
            userBean.setPassword(newpassword);
            userMapper.save(userBean);
            return "Success";
        } else {//用户当前密码不是初始密码
            userBean.setPassword(newpassword);
            userMapper.save(userBean);
            return "Success";
        }
    }

    @Override
    public void createAdmin() {//判断数据库中是否有管理员，若无，自动生成
        UserBean userbean = userMapper.findByUsertype("admin");
        if (userbean == null) {
            UserBean admin = new UserBean();
            admin.setUsertype("admin");
            admin.setSchoolnumber("10001");
            admin.setName("admin");
            admin.setPassword("admin666");
            admin.setIdentitynumber("310101197001010011");
            userMapper.save(admin);
            System.out.println("Successfully created initial administrator!");
        }
    }


    @Override
    public void deleteMajortest(int majornumber) {
        Major major = majorMapper.findByMajornumber(majornumber);
        majorMapper.delete(major);
    }


    @Override
    public void createMajortest() {//测试函数，会在运行开始时自动执行，用以测试


        Major major = new Major();
        major.setSchool("计算机学院");
        major.setName("计算机科学与技术");
//        List<Major> majorList = majorMapper.findAll();
//        Major maxmajor = majorList.stream().max(Comparator.comparing(Major::getMajornumber)).get();
        major.setMajornumber(10);
        majorMapper.save(major);
        int a = major.getMajornumber();
        System.out.println("created major with number");
        System.out.println(a);

    }

    @Override
    public String createMajor(Major major) {//创建一个对象，若主键已存在，则修改对象的值

        /*在此处增加校验major成员格式的代码，假定传入进来major的成员都不是null。
         * 并修改函数返回值。
         * 成功返回"Success"，
         * 格式错误返回"FormError"，
         * 其他错误返回"UnknownError"。
         * 要求school，name都不能为空字符串。
         * */
        Major a = majorMapper.save(major);

        if (a == null) {
            return "UnknownError";
        }


        if (a.getMajornumber() == 0) {
            return "FormError";
        }
        if (a.getName() == null) {
            return "FormError";
        }
        if (a.getSchool() == null) {
            return "FormError";
        }


        return "Success";
    }

    @Override
    public String deleteMajor(int majornumber) {//删除一个对象

        Major major = majorMapper.findByMajornumber(majornumber);

        if (major != null) {
            majorMapper.delete(major);
            return "success";
        } else {
            return "nonexistent";
        }
    }

    @Override
    public String getANewMajornumber() {//返回一个可用的majornumber(str)
        List<Major> majorList = majorMapper.findAll();
        Major maxmajor = majorList.stream().max(Comparator.comparing(Major::getMajornumber)).get();
        /*
         * 在此处增加异常处理：
         * 根据idea提示，如果列表为空，找不到最大majornumber，会抛出异常
         * 在本函数内捕获该异常。出现此异常时，应返回的majornumber为"1"
         * */
        if (majorList.isEmpty()) {
            return String.valueOf(1);
        }

        System.out.println("getMaxMajornumber=");
        System.out.println(maxmajor.getMajornumber());

        return String.valueOf(maxmajor.getMajornumber() + 1);
    }

    @Override
    public String[] getAllMajornumbers() {
/*
在此处增加相关代码，要求以String数组的形式，返回表中所有的majornumber
 */
        List<Major> majorList = majorMapper.findAll();

        String[] majorNumbersArray = new String[majorList.size()];

        for (int i = 0; i < majorList.size(); i++)
            majorNumbersArray[i] = String.valueOf(majorList.get(i));

        return majorNumbersArray;
    }

    @Override
    public Major getAMajor(int majornumber) {
        /*
         * 在此处增加相关代码，通过majornumber查询major
         * 若存在此majornumber，返回该major对象，
         * 若不存在，返回null。
         * */

        List<Major> majorList = majorMapper.findAll();

        for (int i = 0; i < majorList.size(); i++)
            if(majorList.get(i).getMajornumber()==majornumber){
                return majorList.get(i);
            }
        return null;
    }


}