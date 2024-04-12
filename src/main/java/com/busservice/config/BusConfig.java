package com.busservice.config;

import com.busservice.client.DriverClient;
//import com.busservice.client.DriverClientImp;
//import com.busservice.client.DriverClientImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BusConfig {

    @Bean
    public  RestTemplate restTemplate(){
        return  new RestTemplate();
    }
    @Bean(name = "customPasswordEncoder")
    public PasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
