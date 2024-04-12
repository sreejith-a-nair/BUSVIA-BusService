package com.busservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;
    private  String couponName;
    @Column(unique = true)
    private  String couponCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    private  int count;
    private double maxPrice;
    private int offPercent;
    private int minFare;
    private boolean enabled ;

}
