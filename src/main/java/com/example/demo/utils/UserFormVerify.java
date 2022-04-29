package com.example.demo.utils;

import java.util.Objects;

public class UserFormVerify {
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    private static final String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    private static final String REGEX_PHONE_1 = "1\\d{10}";

    public boolean id_verify(String id){
        return id.matches(REGEX_ID_CARD18);
    }

    public boolean email_verify(String email){

        return (email == "") || email.matches(REGEX_EMAIL);
    }

    public boolean phone_verify(String phone){
        return (phone == "") || phone.matches(REGEX_PHONE_1);
    }

    public boolean password_verify(String password){
        int len = password.length();
        int kinds = (password.matches(".*\\d+.*") ? 1 :0 )
                +(password.matches(".*[a-zA-Z]+.*") ? 1 : 0)
                +(password.matches(".*[-_]+.*") ? 1 : 0);
        boolean isillegal = password.matches(".*[^[-\\w]]+.*");//匹配到不支持的字符

        return 6 <= len && len <= 32 && kinds >= 2 && !isillegal;
    }
    public boolean name_verify(String name){
        return name.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文

    }
    public boolean workid_verify(String number){
        return number.matches("((0\\d)|(1\\d)|(2[0-2]))\\d{6}");//前两位00~22，共八位
    }
    public boolean stuid_verify(String number){
        return number.matches("((0\\d)|(1\\d)|(2[0-2]))\\d{4}");//前两位00~22，共六位
    }
    public boolean school_verify(String school){
        return school.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文
    }
    public boolean major_verify(String major){
        return major.matches("[\u4e00-\u9fa5A-Za-z]+");//中英文
    }


}
