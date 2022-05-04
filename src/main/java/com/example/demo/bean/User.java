package com.example.demo.bean;

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

}
