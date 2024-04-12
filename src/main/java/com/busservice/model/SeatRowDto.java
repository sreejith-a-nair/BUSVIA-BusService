package com.busservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRowDto {

//    @JsonProperty("row")
    private String row;

//    @JsonProperty("numberOfSeats")
    private Integer numberOfSeats;

    // Constructors, getters, and setters
}

