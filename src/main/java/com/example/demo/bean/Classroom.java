package com.example.demo.bean;

import com.example.demo.annotations.ClassroomCapacity;
import com.example.demo.bean.jsonUtils.IntegerToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@ClassroomCapacity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classroom")
@Entity
public class Classroom {//admin changeable

    @Id
    @JsonSerialize(using = IntegerToStringSerializer.class)
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

    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.DELETE})
    @OneToMany(mappedBy = "classroom")
    private List<Classarrange> classarranges;
}