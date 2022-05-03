package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    @Column(name="majorid", nullable = false, length = 5)
    private String majorid;//unchangeable

    @Column(name="name", nullable = false, length = 32)
    private String name;//changeable

    @JsonDeserialize(using = MajorDeserializer.class)
    @JsonIgnoreProperties(value = "majors")
    @ManyToOne
    @JoinColumn(name= "major_schoolid")
    private School school;//changeable

}
