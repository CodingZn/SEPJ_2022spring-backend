package com.example.demo.bean;

import com.example.demo.bean.jsonUtils.LessonDeserializer;
import com.example.demo.bean.jsonUtils.LessonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "arrange")
public class Classarrange {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "classtime", nullable = false)
    private Classtime classtime;

    @ManyToOne
    @JoinColumn(name = "classroom", nullable = false)
    private Classroom classroom;

    @JsonSerialize(using = LessonSerializer.class)
    @JsonDeserialize(using = LessonDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "uplesson")
    private Lesson uplesson;
}
