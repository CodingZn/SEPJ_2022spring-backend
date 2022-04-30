package com.example.demo.bean.trivialBeans;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classtime")
@Entity
public class Classtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private int classtimeid;

    @Column(nullable = false, length = 5)
    private String name;

    @Column(nullable = false, length = 20)
    private String time;

}
