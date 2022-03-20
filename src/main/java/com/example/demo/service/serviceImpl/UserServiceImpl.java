package com.example.demo.service.serviceImpl;

import com.example.demo.service.UserService;
import com.example.demo.bean.UserBean;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    //将DAO(Mapper)层注入Service层
    @Autowired
    public UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public UserBean login(String schoolnumber, String password) {
        return userMapper.findBySchoolnumberAndPassword(schoolnumber, password);
    }

    @Override
    public String register(UserBean userBean) {
        String schoolnumber, newidentitynumber;
        UserBean a,b;
        schoolnumber=userBean.getSchoolnumber();
        newidentitynumber=userBean.getIdentitynumber();
        a = userMapper.findBySchoolnumber(schoolnumber);
        b = userMapper.findByIdentitynumber(newidentitynumber);
        if (a != null)
            return "SchoolnumberConflict";
        else if (b != null)
            return "IdentitynumberConflict";
        else{
            userBean.setPassword("fDu666666");//统一设置初始密码
            if (userBean.verifyform()){
                userMapper.save(userBean);
                return "Success";
            }else
                return "FormError";

        }


    }


    @Override
    public String changePassword(String schoolid, String newpassword) {
        UserBean userBean;
        userBean = userMapper.findBySchoolnumber(schoolid);

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
        if (userbean == null){
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


}