package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classroom")
@Entity
public class Classroom {//admin changeable

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private int classroomid;//unchangeable

    @Column(nullable = false, length = 20, unique = true)
    private String name;//admin changeable

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.enabled;//admin changeable

    public enum Status{
        enabled, disabled
    }

    @Column
    private Integer capacity;//admin changeable


}
