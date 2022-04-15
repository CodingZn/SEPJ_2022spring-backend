package com.example.demo.service.serviceImpl;

import com.example.demo.service.UserSpecService;
import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSpecServiceImpl implements UserSpecService {
    private final UserMapper userMapper;

    //将DAO(Mapper)层注入Service层
    @Autowired
    public UserSpecServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void createAdmin() {//判断数据库中是否有管理员，若无，自动生成
        User userbean = userMapper.findByUsertype("admin");
        if (userbean == null) {
            User admin = new User();
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
    public User login(String schoolnumber, String password) {
        return userMapper.findBySchoolnumberAndPassword(schoolnumber, password);
    }


}