package com.busservice.model;

import com.busservice.entity.SeatsRows;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BusResponse {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("busName")
    private  String busName;

    @JsonProperty("busNumber")
    private  String busNumber;

    @JsonProperty("busType")
    private  String busType;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonProperty("totalSeats")
    private int totalSeats;

    @JsonProperty("availableSeats")
    private int availableSeats;

    @JsonProperty("fare")
    private double fare;

    @JsonProperty("category")
    private String category;

    @JsonProperty("doubleSeatCount")
    private int doubleSeatCount;

    @JsonProperty("thirdRowSeatCount")
    private int thirdRowSeatCount;

    @JsonProperty("upperSeat")
    private int upperSeat;

    @JsonProperty("lowerSeat")
    private  int lowerSeat;

    @JsonProperty("email")
    private String email;

    @JsonProperty("seatsRows")
    private List<SeatsRows> seatsRows;



    // Getters and setters omitted
}