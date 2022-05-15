package com.example.demo.service.serviceImpl;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements GeneralService<User> {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /* ************************************ */

    @Override
    public List<String> getAllIds() {
        List<User> userList = userMapper.findAll();
        return userList.stream().map(User::getUserid).toList();
    }

    @Override
    public User getABean(String userid) {
        return userMapper.findByUserid(userid);
    }

    @Override
    public List<User> getAllBeans() {
        return userMapper.findAll();
    }

    @Override
    public String createABean(User user) {

        user.setPassword("fDu666666");//统一设置初始密码
        if (user.getUsertype() == User.Type.admin){
            return "ConflictAdmin";
        }
        userMapper.save(user);
        return "Success";

    }

    @Override
    public String createBeans(List<User> beans) {
        beans.removeIf(Objects::isNull);
        for(User user : beans){
            createABean(user);
        }
        return "Success";
    }

    @Override
    public String changeABean(String userid,User user) {
        User user1 = userMapper.findByUserid(userid);
        if (user1 == null)
            return "NotFound";

        user.setUserid(userid);
        userMapper.save(user);
        return "Success";
    }

    @Override
    public String deleteABean(String userid) {
        User user = userMapper.findByUserid(userid);

        if (user != null) {
            userMapper.delete(user);
            return "Success";
        } else {
            return "NotFound";
        }
    }

    @Override
    public String deleteBeans(List<?> ids) {
        List<String> userids = (List<String>) ids;
        for(String userid : userids) {
            userMapper.deleteById(userid);
        }
        return "Success";
    }
}
