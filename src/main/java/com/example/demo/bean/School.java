package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "school")
public class School {

    @Id
    @Column(name="schoolid", nullable = false, length = 5)
    private String schoolid;

    @Column(name="name", nullable = false, length = 32)
    private String name;

    @OneToMany(mappedBy = "school")
    private List<Major> majors;

}
