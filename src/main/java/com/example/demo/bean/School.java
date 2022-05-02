package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "school")
public class School {//admin changeable

    @Id
    @Column(name="schoolid", nullable = false, length = 5)
    private String schoolid;//unchangeable

    @Column(name="name", nullable = false, length = 32)
    private String name;//changeable

    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<Major> majors;//changeable

}
