package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "school")
@GenericGenerator(name = "schoolidGenerator",
        strategy = "com.example.demo.bean.generators.SchoolidGenerator")
public class School {//admin changeable

    @Id
    @GeneratedValue(generator = "schoolidGenerator")
    @Column(name="schoolid", nullable = false, length = 5)
    private String schoolid;//unchangeable

    @Column(name="name", nullable = false, length = 32)
    private String name;//changeable

    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<Major> majors;//changeable

}
