package com.example.demo.service.serviceImpl;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements GeneralService<User> {
    private final UserMapper userMapper;
    private final DependValueVerify dependValueVerify;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, DependValueVerify dependValueVerify) {
        this.userMapper = userMapper;
        this.dependValueVerify = dependValueVerify;
    }

    @Override
    public List<String> getAllIds() {
        List<User> userList = userMapper.findAll();
        return userList.stream().map(User::getSchoolnumber).toList();
    }

    @Override
    public User getABean(String schoolnumber) {
        return userMapper.findBySchoolnumber(schoolnumber);
    }

    @Override
    public List<User> getAllBeans() {
        return null;
    }

    @Override
    public String createABean(String schoolnumber, User user) {
//        b = userMapper.findByIdentitynumber(newidentitynumber);//是否检查身份证号冲突

        User user1;
        user1 = userMapper.findBySchoolnumber(schoolnumber);
        if (user1 != null)
            return "Conflict";
        user.setSchoolnumber(schoolnumber);
        user.setPassword("fDu666666");//统一设置初始密码

        if (!user.verifyform()) {//检查数据格式
            return "FormError";
        }
        else if (!dependValueVerify.userDependCheck(user)){//检查依赖属性
            return "DependError";
        }
        else{
            userMapper.save(user);
            return "Success";
        }

    }

    @Override
    public String changeABean(String schoolnumber, User user) {
        User user1 = userMapper.findBySchoolnumber(schoolnumber);
        if (user1 == null)
            return "NotFound";

        user.setSchoolnumber(schoolnumber);//保证id不变，是修改而非新增
        if (!user.verifyform()) {
            return "FormError";
        }
        else if (!dependValueVerify.userDependCheck(user)){
            return "DependError";
        }
        else{
            userMapper.save(user);
            return "Success";
        }
    }

    @Override
    public String deleteABean(String schoolnumber) {
        User user = userMapper.findBySchoolnumber(schoolnumber);

        if (user != null) {
            userMapper.delete(user);
            return "Success";
        } else {
            return "NotFound";
        }
    }
}
