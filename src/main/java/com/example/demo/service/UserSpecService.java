package com.example.demo.service;

import com.example.demo.bean.User;

public interface UserSpecService {

    void createAdmin();

    User login(String schoolnumber, String password);

}