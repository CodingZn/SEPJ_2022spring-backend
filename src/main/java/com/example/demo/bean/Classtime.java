package com.example.demo.bean;

import com.example.demo.bean.jsonUtils.IntegerToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classtime")
@Entity
public class Classtime {//admin changeable

    @Id
    @JsonSerialize(using = IntegerToStringSerializer.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private int classtimeid;//unchangeable

    @Pattern(regexp = "([一二三四五六日]([1-9]|1[0-4]),)*([一二三四五六日]([1-9]|1[0-4]))+", message = "节次格式错误！")
    @Column(nullable = false, length = 5, unique = true)
    private String name;//admin changeable

    @Pattern(regexp = "(([01]\\d)|(2[0-3])):([0-5]\\d)-(([01]\\d)|(2[0-3])):([0-5]\\d)", message = "时间格式错误！")
    @Column(nullable = false, length = 20)
    private String time;//admin changeable

    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.DELETE})
    @OneToMany(mappedBy = "classtime")
    private List<Classarrange> classarranges;

}
