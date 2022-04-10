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
import java.util.stream.Collectors;

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

        if (major == null) {
            return "UnknownError";
        }

        if (major.getMajornumber() <= 0 || Objects.equals(major.getName(), "") || Objects.equals(major.getSchool(), "")) {
            return "FormError";
        }

        Major a = majorMapper.save(major);
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


        try {
            Major maxmajor = majorList.stream().max(Comparator.comparing(Major::getMajornumber)).get();
            System.out.println("getMaxMajornumber=");
            System.out.println(maxmajor.getMajornumber());

            return String.valueOf(maxmajor.getMajornumber() + 1);
        }catch (Exception e){
            return "1";
        }
    }

    @Override
    public List<String> getAllMajornumbers() {

        List<Major> majorList = majorMapper.findAll();

        List<String> a = majorList.stream().map( u -> String.valueOf(u.getMajornumber())).toList();

        System.out.println("a"+a);

        return a;
    }

    @Override
    public Major getAMajor(int majornumber) {
        return majorMapper.findByMajornumber(majornumber);
    }

    @Override
    public List<String> getAllSchoolnumbers() {
        List<UserBean> userBeanList = userMapper.findAll();
        List<String> a = userBeanList.stream().map(UserBean::getSchoolnumber).toList();
        return a;
    }

    @Override
    public UserBean getAUser(String schoolnumber) {
        return userMapper.findBySchoolnumber(schoolnumber);
    }

    @Override
    public String deleteUser(String schoolnumber) {
        UserBean userBean = userMapper.findBySchoolnumber(schoolnumber);

        if (userBean != null) {
            userMapper.delete(userBean);
            return "success";
        } else {
            return "nonexistent";
        }
    }

    @Override
    public String rewriteUser(UserBean userBean){
        String id_old = userMapper.findBySchoolnumber(userBean.getSchoolnumber()).getId();
        userBean.setId(id_old);//保证id不变，是修改而非新增
        if (userBean.verifyform()) {
            userMapper.save(userBean);
            return "Success";
        } else
            return "FormError";
    }

}