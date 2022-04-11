package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @Column(name = "lessonid", nullable = false)
    private int lessonid;

    @Column(name = "lessonname")
    private String lessonname;

    @Column(name = "school")
    private String school;

    @Column(name = "hour")
    private String hour;

    @Column(name = "credit")
    private String credit;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "period")
    private String period;

    @Column(name = "place")
    private String place;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "status")
    private String status;


}
