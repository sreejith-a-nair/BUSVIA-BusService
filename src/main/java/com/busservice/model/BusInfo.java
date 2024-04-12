package com.busservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusInfo {

    private UUID uuid;
    private  String busName;
    private  String busNumber;
    private  String busType;
    private int totalSeats;
    private int availableSeats;
    private double fare;
    private String category;
    private int doubleSeatCount;
    private int thirdRowSeatCount;
    private String email;
    private int upperSeat;
    private  int lowerSeat;

}
