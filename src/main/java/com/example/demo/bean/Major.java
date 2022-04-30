package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="major")
public class Major {

    @Id
    @Column(name="majorid", nullable = false, length = 5)
    private String majorid;

    @Column(name="name", nullable = false, length = 32)
    private String name;

    @ManyToOne
    @JoinColumn(name= "major_schoolid")
    private School school;

}
