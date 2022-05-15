package com.example.demo.annotations.constraintclass;

import com.example.demo.annotations.UserPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<UserPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        int len = password.length();
        int kinds = (password.matches(".*\\d+.*") ? 1 :0 )
                +(password.matches(".*[a-zA-Z]+.*") ? 1 : 0)
                +(password.matches(".*[-_]+.*") ? 1 : 0);
        boolean isillegal = password.matches(".*[^[-\\w]]+.*");//匹配到不支持的字符

        return 6 <= len && len <= 32 && kinds >= 2 && !isillegal;
    }
}
