package com.busservice.model;

import com.busservice.entity.BusRootAndTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusStopRequest {

    @ManyToOne
    @JoinColumn(name = "route_id")
    private BusRootAndTime route;

    private String stopName;

    private int sequenceNumber;

    private String routeId;
}