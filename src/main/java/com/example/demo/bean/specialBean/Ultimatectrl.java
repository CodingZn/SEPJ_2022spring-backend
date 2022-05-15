package com.example.demo.bean.specialBean;
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

    public static final String KEY_CLASS_CONTROL = "classControl";
    public static final String VALUE_CLASS_CONTROL_DISABLED = "disabled";
    public static final String VALUE_CLASS_CONTROL_FIRST = "first";
    public static final String VALUE_CLASS_CONTROL_SECOND = "second";

    public static final String REGEX_CLASS_CONTROL =
            VALUE_CLASS_CONTROL_DISABLED + "|" +
            VALUE_CLASS_CONTROL_FIRST + "|" +
            VALUE_CLASS_CONTROL_SECOND;

    public static final String KEY_SEMESTER_CONTROL = "semester";
    public static final String REGEX_SEMESTER_CONTROL = "(19|20)\\d{2}[AB]";

    public static final String KEY_YEAR_CONTROL = "year";
    public static final String REGEX_YEAR_CONTROL = "\\d{2}";

    public static final String KEY_LATEST_GRADE_CONTROL = "latestGrade";
    public static final String KEY_OLDEST_GRADE_CONTROL = "oldestGrade";
    public static final String REGEX_GRADE_CONTROL = "\\d{2}";

    @Id
    @Column(nullable = false, length = 20, updatable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 20)
    private String value;
}

