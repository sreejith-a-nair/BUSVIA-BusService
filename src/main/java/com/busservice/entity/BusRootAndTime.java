package com.busservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusRootAndTime {
    @Id
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

    @ManyToOne
    @JoinColumn(name = "busInfo")
    @JsonIgnore
    private BusEntity busInfo;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<BusStopEntity> stops;


    @Override
    public String toString() {
        return "BusRootAndTime{" +
                "uuid=" + uuid +
                ", sourceLocation='" + sourceLocation + '\'' +
                ", destinationLocation='" + destinationLocation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", totalHour='" + totalHour + '\'' +
                ", perdayTrip=" + perdayTrip +
                ", busId=" + busId +
                '}';
    }


}
