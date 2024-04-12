package com.busservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    private  String busId;
    private List<SeatRowDto> seatRows;
}

