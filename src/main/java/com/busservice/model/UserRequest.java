package com.busservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private UUID uuid;

    private  String firstName;

    private  String email;

    private  String password;

    private  String role;

    private String AuthorityName;

    private String contact;

//    public UserRequest(UUID uuid, String busName, String busNumber, String busType, int totalSeats, int doubleSeatCount, int thirdRowSeatCount, String email) {
//    }
}