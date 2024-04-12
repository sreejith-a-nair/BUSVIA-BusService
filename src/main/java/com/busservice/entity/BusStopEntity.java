package com.busservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusStopEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        private UUID uuid;

        @ManyToOne
        @JoinColumn(name = "route_id")
        private BusRootAndTime route;

        private String stopName;

        private int sequenceNumber;
    }
