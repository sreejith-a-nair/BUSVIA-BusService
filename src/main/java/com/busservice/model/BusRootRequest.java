package com.busservice.model;

import com.busservice.entity.BusEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusRootRequest {
    private UUID uuid;
    private  String sourceLocation ;
    private  String destinationLocation;
    private String departureTime;
    private String arrivalTime;
    private Date departureDate;
    private Date arrivalDate;
    private String totalHour;
    private int perdayTrip;
    private String rootType;

    @Column(name = "bus_id")
    private UUID busId;

//    @ManyToOne
//    @JoinColumn(name = "busInfo")
//    private BusEntity busInfo;
}
