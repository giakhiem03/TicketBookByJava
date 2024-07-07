package com.example.Project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User_HoiThao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hoithao_id")
    private HoiThao hoiThao;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @NotBlank(message = "studentCode is required")
    private String studentCode;
    @NotBlank(message = "school is required")
    private String school;
    @NotBlank(message = "faculty is required")
    private String faculty;
    @NotBlank(message = "myClass is required")
    private String myClass;
    @NotBlank(message = "familyName is required")
    private String familyName;
    @NotBlank(message = "lastName is required")
    private String lastName;
}
