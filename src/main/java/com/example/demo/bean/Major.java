package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="major")
public class Major {

    @Id
    @Column(name="majornumber", nullable = false)
    private int majornumber;

    @Column(name="name")
    private String name;

    @Column(name = "school")
    private String school;



}
