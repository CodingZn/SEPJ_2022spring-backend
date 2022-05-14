package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classtime")
@Entity
public class Classtime {//admin changeable

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private int classtimeid;//unchangeable

    @Column(nullable = false, length = 5, unique = true)
    private String name;//admin changeable

    @Column(nullable = false, length = 20)
    private String time;//admin changeable

}
