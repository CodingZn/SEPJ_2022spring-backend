package com.example.demo.bean;

import com.example.demo.annotations.LessonCapacity;
import com.example.demo.bean.jsonUtils.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@LessonCapacity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {//changeable

    @Id
    @JsonSerialize(using = IntegerToStringSerializer.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lessonid", nullable = false, unique = true, updatable = false)
    private int lessonid;//unchangeable

    @Pattern(regexp = "[A-Z]{4}\\d{6}\\.\\d{2}", message = "课程序号格式不对！")
    @Column(name = "lessonnumber", nullable = false, length = 20, updatable = false)
    private String lessonnumber;//unchangeable

    @Pattern(regexp = "[A-Z]{4}\\d{6}", message = "课程代码格式不对！")
    @Column(name = "lessoncode", nullable = false, length = 15, updatable = false)
    private String lessoncode;//unchangeable

    @Pattern(regexp = "[\u4e00-\u9fa5A-Za-z]+", message = "课程名只能包括中英文！")
    @Column(name = "lessonname", nullable = false, length = 32)
    private String lessonname;//admin|teacher_self changeable

    @NotNull(message = "所属学院不能为空")
    @JsonDeserialize(using = SchoolDeserializer.class)
    @JsonSerialize(using = SchoolSerializer.class)
    @ManyToOne
    @JoinColumn(name = "school")
    private School school;//admin changeable

    @Range(min = 1, message = "必须为正整数")
    @Column(name = "hour", nullable = false)
    private Integer hour;//admin changeable

    @Range(min = 1, message = "必须为正整数")
    @Column(name = "credit", nullable = false)
    private Integer credit;//admin changeable

    @Cascade({org.hibernate.annotations.CascadeType.DETACH})
    @JsonDeserialize(using = UserListDeserializer.class)
    @JsonSerialize(using = UserListSerializer.class)
    @ManyToMany
    @JoinTable(name="lessons_teacher_taking")
    private List<User> teacher;//admin changeable

    @Column(name = "introduction")
    private String introduction;//admin|teacher_self changeable

    @Cascade({org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REMOVE})
    @JsonDeserialize(using = ClassarrangeListDeserializer.class)
    @JsonSerialize(using = ClassarrangeListSerializer.class)
    @OneToMany
    @JoinColumn(name = "uplesson")
    private List<Classarrange> classarrange;//admin changeable

    @Column(name = "capacity")
    private Integer capacity;//admin changeable

    @Pattern(regexp = "20\\d{2}[ABCD]", message = "学期格式错误！")
    @Column(name = "semester", length = 10)
    private String semester;//unchangeable

    @Pattern(regexp = "((((0\\d)|(1\\d)|(2[0-2]))-\\d{3},)*(((0\\d)|(1\\d)|(2[0-2]))-\\d{3}))|all", message = "格式错误！")
    @Column(name = "majorallowed")
    private String majorallowed;//admin changeable

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.censored;//admin changeable

    public enum Status{
        censored, pending
    }


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="lessons_students_taking")
    private List<User> classmates;//admin|student_self changeable


}
