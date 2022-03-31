package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="major")
//@GenericGenerator(name="genmajor", strategy = "uuid")
public class Major {

    @Id
    @Column(name="majornumber", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int majornumber;

    @Column(name="name")
    private String name;

    @Column(name = "school")
    private String school;



}
