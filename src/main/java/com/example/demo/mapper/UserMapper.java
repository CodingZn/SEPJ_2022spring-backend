package com.example.demo.mapper;

import com.example.demo.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<User, String> {

    User findByUseridAndPassword(String userid, String password);
    User findByUserid(String userid);
    User findByIdentitynumber(String identitynumber);
    User findByUsertype(User.Type usertype);

}