package com.busservice.config;

import com.busservice.client.DriverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

    @Configuration
    public class WebClientConfig {

        @Autowired
        private LoadBalancedExchangeFilterFunction filterFunction;


        @Bean
        public WebClient userClient(){
            return WebClient.builder()
                    .baseUrl("http://user-service")
                    .filter(filterFunction)
                    .build();
        }


//        @Bean
//        public DriverClient driverClient(){
//
//            HttpServiceProxyFactory httpServiceProxyFactory
//                    = HttpServiceProxyFactory
//                    .builder(WebClientAdapter.forClient(userClient()))
//                    .build();
//            return  httpServiceProxyFactory.createClient(DriverClient.class);
//
//        }
        @Bean
        public DriverClient driverClient(WebClient userClient) {
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                    .builder(WebClientAdapter.forClient(userClient))
                    .build();
            return  httpServiceProxyFactory.createClient(DriverClient.class);
        }

    }
