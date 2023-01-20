package org.viamatica.school.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.sql.Date;

@Entity(name = "course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Size(max = 100)
    @Column(name = "title")
    String title;
    @Size(max = 250)
    @Column(name = "descriptionCourse")
    String descriptionCourse;
    @Column(name = "dateStrat")
    Date dateStrat;
    @Column(name = "dateEnd")
    Date dateEnd;
    @Column(name = "price")
    float price;
    @Column(name = "status")
    String status;

}
