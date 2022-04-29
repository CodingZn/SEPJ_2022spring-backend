package com.example.demo.bean.trivialBeans;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classtime")
@Entity
public class Classtime {

    @Id
    @Column(nullable = false, length = 5)
    private String name;

    @Column(nullable = false, length = 20)
    private String time;

}
