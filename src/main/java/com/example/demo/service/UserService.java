package com.example.demo.service;

import com.example.demo.bean.UserBean;
import com.example.demo.bean.Major;

public interface UserService {

    UserBean login(String schoolnumber, String password);
    String register(UserBean userBean);
    String changePassword(String schoolnumber, String newpassword);
    void createAdmin();



    void deleteMajortest(int majornumber);

    void createMajortest();

    String createMajor(Major major);

    String deleteMajor(int majornumber);

    String getANewMajornumber();

    String[] getAllMajornumbers();

    Major getAMajor(int majornumber);
}