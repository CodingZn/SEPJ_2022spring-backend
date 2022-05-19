package com.example.demo.annotations.constraintclass;

import com.example.demo.annotations.UserInfo;
import com.example.demo.bean.User;
import com.example.demo.exceptions.MyException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserInfoConstraintValidator implements ConstraintValidator<UserInfo, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext){
        if (user.getUsertype() == User.Type.student){
            if (user.getSchool() == null)
                return false;
            if (user.getMajor() == null)
                return false;
            if (user.getGrade() == null)
                return false;
        }
        else if (user.getUsertype() == User.Type.teacher){
            if (user.getSchool() == null)
                return false;
        }
        return true;
    }
}
