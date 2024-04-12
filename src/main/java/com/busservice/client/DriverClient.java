package com.busservice.client;
import com.busservice.model.BusBlockRequest;
import com.busservice.model.BusRequest;
import com.busservice.model.BusRootReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
@HttpExchange
public interface DriverClient {

    @PostExchange("/user/addBus")
    String addBus(@RequestBody BusRequest busRequest);

    @PostExchange("/user/updateBus")
    String updateBus(@RequestBody BusRequest busRequest);

    @PostExchange("/user/blockBus")
    String blockBus(@RequestBody BusBlockRequest busBlockRequest);


    @PostExchange("/user/unblockBus")
    String unblockBus(@RequestBody BusBlockRequest busBlockRequest);

    @PostExchange("/user/updateRoot")
    String updateRoot(@RequestBody BusRootReq busRootReq);



    default void fallback(Exception e){
        throw new RuntimeException("Fallback response for adding a new user.");
    }
}

