package com.busservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequest {
    private String departurePlace;
    private String arrivalPlace;
    private Date departureDate;
}
