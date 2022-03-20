package com.example.demo.service;

import com.example.demo.bean.UserBean;

public interface UserService {

    UserBean login(String schoolnumber, String password);
    String register(UserBean userBean);
    String changePassword(String schoolnumber, String newpassword);
    void createAdmin();

}