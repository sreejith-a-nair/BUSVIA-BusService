//package com.busservice.client;
//
//import com.busservice.model.BusRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class DriverClientImp  implements DriverClient{
//
//    private final RestTemplate restTemplate;
//
//    public DriverClientImp(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//    @Override
//    public String addBus(BusRequest busRequest) {
//        ResponseEntity<String> response = restTemplate.postForEntity("/user/addBus", busRequest, String.class);
//        return response.getBody();
//    }
//}
