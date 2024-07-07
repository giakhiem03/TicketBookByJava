package com.example.Project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HoiThao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "detail is required")
    private String detail;
    private String image;
    @NotBlank(message = "address is required")
    private String address;
    @NotNull(message = "seatNumber is required")
    private int seatNumber;
    @NotNull(message = "Nhập vào thời gian")
    private LocalDateTime time;
    @NotNull(message = "price is required")
    private double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private CategoryHoiThao category;
}
