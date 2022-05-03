package com.example.demo.service;

import com.example.demo.bean.User;

import java.util.List;

public interface UserSpecService {

    void createAdmin();

    User login(String userid, String password);

    List<User> getAllByUsertype(User.Type usertype);

}