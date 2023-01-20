package org.viamatica.school.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity(name = "userCourse")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "userName")
    String userName;
    @Column(name = "userPassword")
    String userPassword;
    @Column(name = "email")
    String email;
    @Column(name = "statusCourse")
    String statusCourse;
    @Column(name = "idRol")
    int idRol;
}
