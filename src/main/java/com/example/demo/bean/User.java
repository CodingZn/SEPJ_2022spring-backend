package com.example.demo.bean;

import com.example.demo.annotations.UserPassword;
import com.example.demo.bean.jsonUtils.MajorDeserializer;
import com.example.demo.bean.jsonUtils.MajorSerializer;
import com.example.demo.bean.jsonUtils.SchoolDeserializer;
import com.example.demo.bean.jsonUtils.SchoolSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@GenericGenerator(name = "usergenerator",
        strategy = "com.example.demo.bean.generators.UseridGenerator",
        parameters = {@org.hibernate.annotations.Parameter(name = "User", value = "usertype")})
public class User {//admin|self changeable

    public enum Type {
        admin, teacher, student
    }
    public enum Status{
        enabled, disabled
    }


    @Column(name = "usertype", nullable = false, length = 10, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type usertype;//unchangeable

    @Id
    @GeneratedValue(generator = "usergenerator")
    @Column(name = "userid", nullable = false, length = 32, unique = true, updatable = false)
    private String userid;//unchangeable

    @NotNull(message = "姓名不能为空！")
    @Pattern(regexp = "[\u4e00-\u9fa5A-Za-z]+", message = "姓名只能为中英文字符！")
    @Column(name = "name", nullable = false, length = 64)
    private String name;//admin changeable

    @NotNull(message = "身份证号不能为空！")
    @Pattern(regexp = "^[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0-2]\\d)|3[0-1])\\d{3}([0-9Xx])$", message = "必须为有效身份证号！")
    @Column(name = "identitynumber", nullable = false, length = 32, unique = true)
    private String identitynumber;//unchangeable

    @Email(message = "必须为有效格式！")
    @Column(name = "email", length = 64)
    private String email;//changeable

    @Pattern(regexp = "1\\d{10}", message = "必须为有效手机号！")
    @Column(name = "phonenumber", length = 32)
    private String phonenumber;//changeable

    @NotNull(message = "密码不能为空！")
    @UserPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 32)
    private String password;//self-only changeable

    @JsonSerialize(using = SchoolSerializer.class)
    @JsonDeserialize(using = SchoolDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "school")
    private School school;//admin changeable

    @JsonSerialize(using = MajorSerializer.class)
    @JsonDeserialize(using = MajorDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "major")
    private Major major;//admin changeable

    @Pattern(regexp = "[0-9]{2}", message = "必须为两位数字！")
    @Column(name = "grade", length = 5)
    private String grade;//admin changeable

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false, length = 10)
    private Status status = Status.enabled;//admin changeable

    @JsonIgnore
    @ManyToMany(mappedBy = "classmates")
    private List<Lesson> lessonsTaking;//changeable

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="lessons_students_taken")
    private List<Lesson> lessonsTaken;//admin changeable

    @JsonIgnore
    @OneToMany(mappedBy = "applicant")
    private List<Lessonrequest> lessonrequests;//self-only changeable

    @JsonIgnore
    @ManyToMany(mappedBy = "teacher")
    private List<Lesson> lessonsTakingTea;//changeable
}
