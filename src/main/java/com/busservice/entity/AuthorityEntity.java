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
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;
    private String name;
    private  String email;
    private  String contact;
    private String licenseNumber;
    private String authorityType;
    private String logo;
    private String uniqueName;

    @OneToOne
    @JoinColumn(name = "bus_id")
    private BusEntity bus;


}
