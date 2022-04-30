package com.example.demo.bean;

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

    @ManyToOne
    @JoinColumn(name = "uplesson")
    private Lesson uplesson;
}
