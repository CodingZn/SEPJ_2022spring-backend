package com.example.demo.bean;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.repository.query.Param;

import javax.persistence.*;
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


    @Column(name = "usertype", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Type usertype;//unchangeable

    @Id
    @GeneratedValue(generator = "usergenerator")
    @Column(name = "userid", nullable = false, length = 32)
    private String userid;//unchangeable

    @Column(name = "name", nullable = false, length = 64)
    private String name;//admin changeable

    @Column(name = "identitynumber", nullable = false, length = 32)
    private String identitynumber;//unchangeable

    @Column(name = "email", length = 64)
    private String email;//changeable

    @Column(name = "phonenumber", length = 32)
    private String phonenumber;//changeable

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 32)
    private String password;//self-only changeable

    @JsonIgnoreProperties(value = {"majors"})
    @ManyToOne
    @JoinColumn(name = "school")
    private School school;//admin changeable

    @JsonIgnoreProperties(value = {"school"})
    @ManyToOne
    @JoinColumn(name = "major")
    private Major major;//admin changeable

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
    @JoinTable(name="students_lessons_taken")
    private List<Lesson> lessonsTaken;//admin changeable

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Lessonrequest> lessonrequests;//self-only changeable


//    public boolean verifyform(){
//        UserFormVerify check = new UserFormVerify();
//        boolean a, b, c, d;
//        a = check.name_verify(name)
//                && check.id_verify(identitynumber)
//                && check.password_verify(password)
//                && check.major_verify(major.getName())
//                && check.school_verify(school.getName());
//        b = email == null || check.email_verify(email);
//        c = phonenumber == null || check.phone_verify(phonenumber);
//
//        d = switch (usertype) {
//            case student -> check.stuid_verify(userid);
//            case teacher -> check.workid_verify(userid);
//            case admin -> true;
//        };
//
//        return a && b && c && d;
//
//    }
}
