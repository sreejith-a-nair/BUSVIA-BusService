package com.busservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    private int ratingValue;
    private int ratingCount;
    private double avgRating;
    private UUID userId;
    private  UUID busId;
    private  UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JsonIgnore
    private BusEntity bus;

    @Override
    public String toString() {
        return "Rating{" +
                "uuid=" + uuid +
                ", ratingValue=" + ratingValue +
                ", ratingCount=" + ratingCount +
                ", avgRating=" + avgRating +
                ", userId=" + userId +
                ", busId=" + busId +
                ", bookingId=" + bookingId +
                '}';
    }
}
