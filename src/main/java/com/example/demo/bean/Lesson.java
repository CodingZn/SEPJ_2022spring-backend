package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {//changeable

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lessonid", nullable = false)
    private int lessonid;//unchangeable

    @Column(name = "lessonnumber", nullable = false, length = 20)
    private String lessonnumber;//unchangeable

    @Column(name = "lessoncode", nullable = false, length = 15)
    private String lessoncode;//unchangeable

    @Column(name = "lessonname", nullable = false, length = 32)
    private String lessonname;//admin|teacher_self changeable

    @ManyToOne
    @JoinColumn(name = "school")
    private School school;//admin changeable

    @Column(name = "hour", nullable = false)
    private int hour;//admin changeable

    @Column(name = "credit", nullable = false)
    private int credit;//admin changeable

    @ManyToMany
    @JoinTable(name="lessons_teacher_taking")
    private List<User> teacher;//admin changeable

    @Column(name = "introduction")
    private String introduction;//admin|teacher_self changeable

    @OneToMany(mappedBy = "uplesson")
    private List<Classarrange> arranges;//admin changeable

    @Column(name = "capacity")
    private int capacity;//admin changeable

    @Column(name = "semester", length = 10)
    private String semester;//unchangeable

    @Column(name = "majorallowed")
    private String majorallowed;//admin changeable

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.censored;//admin changeable

    public enum Status{
        censored, pending
    }

    @ManyToMany()
    @JoinTable(name="lessons_students_taking")
    private List<User> classmates;//admin|student_self changeable


}
