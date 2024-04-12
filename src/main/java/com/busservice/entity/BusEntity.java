package com.busservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;
    private  String busName;
    @Column(unique = true)
    private  String busNumber;
    private  String busType;
    private boolean isAvailable;
    private int totalSeats;
    private int availableSeats;
    private double fare;
    private String category;
    private int doubleSeatCount;
    private int thirdRowSeatCount;
    private String email;
    private int upperSeat;
    private  int lowerSeat;



    @OneToMany(mappedBy = "busInfo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BusRootAndTime> busRoots;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<AmenityEntity> amenities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SeatsRows> seatsRows;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings;

}
