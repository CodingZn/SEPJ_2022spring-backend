package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "arrange")
public class Classarrange {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "classtime")
    private Classtime classtime;

    @ManyToOne
    @JoinColumn(name = "classroom")
    private Classroom classroom;

    @JsonIgnoreProperties(value = {"lessonnumber", "lessoncode",
            "lessonname", "school", "hour", "credit", "teacher",
            "introduction", "arranges", "capacity", "semester",
            "majorallowed", "status"})
    @ManyToOne
    @JoinColumn(name = "uplesson")
    private Lesson uplesson;
}
