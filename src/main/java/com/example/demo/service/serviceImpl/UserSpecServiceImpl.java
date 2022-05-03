package com.example.demo.service.serviceImpl;

import com.example.demo.service.UserSpecService;
import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User admin = userMapper.findByUsertype(User.Type.admin);
        if (admin == null) {
            admin = new User();
            admin.setUsertype(User.Type.admin);
            admin.setUserid("10001");
            admin.setName("admin");
            admin.setPassword("admin666");
            admin.setIdentitynumber("310101197001010011");
            admin.setStatus(User.Status.enabled);
            userMapper.save(admin);
            System.out.println("Successfully created initial administrator!");
        }
    }

    @Override
    public User login(String userid, String password) {
        return userMapper.findByUseridAndPassword(userid, password);
    }

    @Override
    public List<User> getAllByUsertype(User.Type usertype) {
        return userMapper.findAllByUsertype(usertype);
    }

}