package com.busservice.model;

import com.busservice.entity.Rating;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusSearchData {
    private UUID uuid;
    private  String busName;
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
    private  String sourceLocation ;
    private  String destinationLocation;
    private String departureTime;
    private String arrivalTime;
    private Date departureDate;
    private Date arrivalDate;
    private String totalHour;
    private int perdayTrip;
    private int upperSeat;
    private  int lowerSeat;
    private Rating rating;


}
