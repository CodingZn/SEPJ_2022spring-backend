package com.example.demo.bean;

import com.example.demo.bean.jsonUtils.SchoolDeserializer;
import com.example.demo.bean.jsonUtils.SchoolSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="major")
@GenericGenerator(name = "majorgenerator", strategy = "com.example.demo.bean.generators.MajoridGenerator")
public class Major {//admin changeable

    @Id
    @GeneratedValue(generator = "majorgenerator")
    @Column(name="majorid", nullable = false, length = 5, unique = true, updatable = false)
    private String majorid;//unchangeable

    @Pattern(regexp = "[\u4e00-\u9fa5A-Za-z]+", message = "名称只能为中英文字符！")
    @Column(name="name", nullable = false, length = 32)
    private String name;//changeable

    @JsonDeserialize(using = SchoolDeserializer.class)
    @JsonSerialize(using = SchoolSerializer.class)
    @ManyToOne
    @JoinColumn(name= "major_schoolid")
    private School school;//changeable

}
