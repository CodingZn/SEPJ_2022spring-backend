package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserBean {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "usertype", length =15, nullable = false)
    private String usertype;
    //usertype变量只允许"admin","teacher","student"三种取值，其余均为非法

    @Column(name = "schoolnumber", nullable = false)
    private String schoolnumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identitynumber", nullable = false)
    private String identitynumber;

    @Column(name = "email")
    private String email;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "school")
    private String school;

    @Column(name = "major")
    private String major;

    @Column(name = "status")
    private String status;



    public boolean verifyform(){
        UserFormVerify check = new UserFormVerify();
        boolean a, b, c, d;
        a = check.utype_verify(usertype)
                && check.name_verify(name)
                && check.id_verify(identitynumber)
                && check.password_verify(password)
                && check.major_verify(major)
                && check.school_verify(school)
                && check.status_verify(status);
        b = email == null || check.email_verify(email);
        c = phonenumber == null || check.phone_verify(phonenumber);

        switch (usertype){
            case "student": d = check.stuid_verify(schoolnumber);break;
            case "teacher": d = check.workid_verify(schoolnumber);break;
            case "admin": d = true;break;
            default: d = false;break;
        }

        return a && b && c && d;

    }
}
