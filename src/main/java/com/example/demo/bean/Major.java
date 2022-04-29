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
    @Column(name="majornumber", nullable = false, length = 5)
    private String majornumber;

    @Column(name="name", nullable = false, length = 32)
    private String name;

    @ManyToOne
    @JoinColumn(name= "major_schoolid")
    private School school;

}
