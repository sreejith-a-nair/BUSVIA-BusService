package com.busservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmenityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String AC;
    private String wifi;
    private boolean restroom;
    private boolean entertainmentSystem;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private BusEntity bus;
}
