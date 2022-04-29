package com.example.demo.bean.trivialBeans;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "controls")
@Entity
public class Ultimatectrl {

    public static final String CLASS_CONTROL = "classcontrol";
    public static final String CLASS_CONTROL_DISABLED = "disabled";
    public static final String CLASS_CONTROL_FIRST = "firstrow";
    public static final String CLASS_CONTROL_SECOND = "secondrow";

    public static final String SEMESTER_CONTROL = "now_semester";

    @Id
    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;
}
/*
* allowed properties and values:
* classcontrol : "disabled", "firstrow", "secondrow"
* now_semester : "2021A"...
* */
