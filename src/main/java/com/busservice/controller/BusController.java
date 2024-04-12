package com.busservice.controller;

import com.busservice.entity.*;
import com.busservice.model.*;
import com.busservice.service.BusService;
import com.busservice.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    BusService busService;

    @Autowired
    CouponService couponService;

    private  static  final Logger LOGGER
            =Logger.getLogger(String.valueOf(BusController.class));

    @PostMapping("/add")
    public ResponseEntity<BusEntity>  addBus(@RequestBody BusEntity busData){

       BusEntity bus= busService.addBus(busData);
        System.out.println("BUS  CATEGORY "+bus.getCategory());
       if (bus!=null){
           LOGGER.info("Add bus Success ");
           return ResponseEntity.status(HttpStatus.CREATED).body(bus);
       }else{
           LOGGER.info("Add bus Failed ");
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bus);
       }

    }
    @PostMapping("/edit-bus/{busId}")
    public ResponseEntity<BusEntity>  updateBus(@RequestBody BusEntity busData,@PathVariable String busId){

        System.out.println("Bus  edit for edit % "+busId);
        System.out.println("Bus  edit for Data %" + busData);
        UUID uuid= UUID.fromString(busId);
        System.out.println("Bus  edit UUID "+uuid);
        BusEntity bus= busService.updateBus(busData,uuid);
        if (bus!=null){
            LOGGER.info("Add bus Success ");
            return ResponseEntity.status(HttpStatus.CREATED).body(bus);
        }else{
            LOGGER.info("Add bus Failed ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bus);
        }

    }

    @PostMapping("/edit-root/{routeId}")
    public ResponseEntity<BusRootAndTime>  updateRoot(@RequestBody BusRootAndTime busRootAndTime,@PathVariable String routeId){

        System.out.println("Bus  edit for edit % "+routeId);
        System.out.println("busRootAndTime  edit for Data %" + busRootAndTime);
        UUID uuid= UUID.fromString(routeId);
        System.out.println("Bus  edit UUID "+uuid);
        BusRootAndTime bus= busService.updateRootAndTime(busRootAndTime,uuid);
        if (bus!=null){
            LOGGER.info("Add bus Success ");
            return ResponseEntity.status(HttpStatus.CREATED).body(bus);
        }else{
            LOGGER.info("Add bus Failed ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bus);
        }

    }
    @DeleteMapping("/deleteBus/{busId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID busId) {
        System.out.println("delete bus "+busId);
        busService  .deleteBus(busId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/addRouteAndTime")
    public ResponseEntity<BusRootAndTime>  addBus(@RequestBody BusRootRequest busRouteAndTimeData){
        LOGGER.info("Bus add:{}");
        System.out.println("BUS ROOT & TIME  DATA WITH BUS UUID "+busRouteAndTimeData);

        BusRootAndTime busRouteAndTime= busService.addBusRootAndTime(busRouteAndTimeData);
        if (busRouteAndTime!=null){
            LOGGER.info("Add bus Success ");
            return ResponseEntity.status(HttpStatus.CREATED).body(busRouteAndTime);
        }else{
            LOGGER.info("Add bus Failed ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(busRouteAndTime);
        }

    }


    @GetMapping("/getAllBus")
    public List<BusEntity> getAllBus(){
        return busService.getALlBus();
    }
    @GetMapping("/getAllBusByEmail")
    public List<BusEntity> getAllBusByMail(@RequestParam(required = false) String email) {
        System.out.println("getBusBy Email working "+ email);
        List <BusEntity> buses= busService.getALlBusByMail(email);
        System.out.println("getBusBy Email Data "+ buses);
        return  buses;
    }

    @PostMapping("/register")
    public String addNewOperator(@RequestBody UserRequest userRequest) {
        System.out.println("BUS REGISTER WORK "+userRequest);
        if(userRequest!=null) {
            busService.saveUser(userRequest);
            return "Operator added successfully in bus service";
        }
        return  "Request is empty";
    }

    @PostMapping("/addBusRootAndTime")
    public String addBusRootAndTime(@RequestBody BusRootRequest busRootRequest) {
        System.out.println("Bus root & time in bus service BUS ID ----_ "+busRootRequest.getBusId());
        if(busRootRequest!=null) {
            busService.addBusRootAndTime(busRootRequest);
            return "Bus root added successfully in bus service";
        }
        return  "Request is empty";

    }

    @PostMapping("/addBusStop")
    public String addBusStop(@RequestBody BusStopRequest busStopRequest) {
        System.out.println("Bus root & time in bus service BUS ID ----_ "+busStopRequest);
        if (busStopRequest != null && busStopRequest.getRouteId() != null) {
            busService.addBusStop(busStopRequest);
            return "Bus stops added successfully.";
        }
        return  "Request is empty";

    }

@PostMapping("/searchBus")
public ResponseEntity<?> searches(@RequestBody SearchRequest searchRequest) {

    List<BusSearchData> busList = busService.getSearchedBuses(searchRequest);
    List<BusSearchData> ActiveBuses = busList.stream()
            .filter(BusSearchData::isAvailable)
            .collect(Collectors.toList());
    System.out.println("SEARCHED LIST: " + ActiveBuses);

    System.out.println("ACTIVE BUS LIST : " + busList);

    return ResponseEntity.ok().body(ActiveBuses);
}
    @PostMapping("/filterBusByFeature")
    public ResponseEntity<?> searches(@RequestBody FeatureRequestForSearch featureRequestForSearch) {
        String response =  featureRequestForSearch.getCategory() ;
        System.out.println("RESPONSE of filter+"+response);


        List<BusSearchData> busList = busService.filterByFeature(featureRequestForSearch);
        System.out.println("SEARCHED LIST : " + busList);

        return ResponseEntity.ok().body(busList);
    }

//    @GetMapping("/findBusById")
//    public BusEntity findBusById(@RequestParam(required = false) String busId) {
//        System.out.println("FIND BUS BY UUID FOR SEAT BOOKING "+ busId);
//        UUID busUuid= UUID.fromString(busId);
//         BusEntity buses= busService.findBusById(busUuid);
//        System.out.println(" Data bus "+ buses);
//        return  buses;
//    }
    @GetMapping("/findBusById")
    public ResponseEntity<BusResponse> findBusById(@RequestParam(required = false) String busId) {
    try {
        UUID busUuid = UUID.fromString(busId);
        BusResponse bus = busService.findBusById(busUuid);

        if (bus == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bus);
    } catch (IllegalArgumentException e) {

        return ResponseEntity.badRequest().build();
    }
}

    @GetMapping("/findBusesByTimeRange")
    public List<BusSearchData> getBusesBetweenTimePeriod(
                                                        @RequestParam String startTime,
                                                        @RequestParam String endTime) {
        System.out.println("startTime" + startTime + "endTime" + endTime);
        return busService.getBusRootAndTimeBetweenTimePeriod(startTime, endTime);
    }
    @PostMapping("/addSeatRows")
    public ResponseEntity<List<SeatsRows>> addSeatRows(@RequestBody Test seatRows) {
        List<SeatsRows> savedSeatRows = busService.addSeatRows(seatRows);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSeatRows);
    }
    @GetMapping("/findBusRouteByBusId")
   public ResponseEntity<BusRootAndTime> findBusRouteByBusId(@RequestParam(required = false) String busId){

        try {
            System.out.println("Bus id "+busId);
            UUID busUuid = UUID.fromString(busId);
            BusRootAndTime bus = busService.findBusRouteByBusId(busUuid);

            if (bus == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bus);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/blockBus/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable String userId) {

        try {
            UUID userUuid = UUID.fromString(userId);
            busService.blockBus(userUuid);
            return ResponseEntity.ok("Bus blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to block bus: " + e.getMessage());
        }
    }

    @PostMapping("/unblockBus/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            busService.unblockBus(userUuid);
            return ResponseEntity.ok("Bus un-blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to un-block Bus: " + e.getMessage());
        }
    }


    @PostMapping("/blockCoupon/{couponId}")
    public ResponseEntity<String> blockCoupon(@PathVariable String couponId) {

        try {
            UUID userUuid = UUID.fromString(couponId);
            couponService.blockCoupon(userUuid);
            return ResponseEntity.ok("Coupon blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to block bus: " + e.getMessage());
        }
    }

    @PostMapping("/unBlockCoupon/{couponId}")
    public ResponseEntity<String> unBlockCoupon(@PathVariable String couponId) {
        try {
            UUID userUuid = UUID.fromString(couponId);
            couponService.unBlockCoupon(userUuid);
            return ResponseEntity.ok("Coupon un-blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to un-block Bus: " + e.getMessage());
        }
    }


    @PostMapping("/updateAvailableSeatAfterCancel")
    public ResponseEntity<String> updateAvailableSeatAfterCancel(
            @RequestParam String busId,
            @RequestParam int canceledSeatCount) {

        UUID busUuid= UUID.fromString(busId);
        System.out.println("Bus id ,"+busUuid);
        System.out.println("Bus  canceledSeatCount ,"+canceledSeatCount);

        System.out.println();
        try {
            busService.updateAvailableSeatAfterCancel(busUuid, canceledSeatCount);
            return ResponseEntity.ok("Available seats updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bus not found with ID: " + busId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update available seats: " + e.getMessage());
        }
    }

    @PostMapping("/updateAvailableSeatAfterBooking")
    public ResponseEntity<String> updateAvailableSeatAfterBooking(
            @RequestParam String busId,
            @RequestParam int bookedSeatCount) {

        UUID busUuid= UUID.fromString(busId);
        System.out.println("Bus id ,"+busUuid);
        System.out.println("Bus  bookedSeatCount ,"+bookedSeatCount);

        System.out.println();
        try {
            busService.updateAvailableSeatAfterBooking(busUuid, bookedSeatCount);
            return ResponseEntity.ok("Available seats updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bus not found with ID: " + busId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update available seats: " + e.getMessage());
        }
    }

    @PostMapping("/addCoupon")
    public  ResponseEntity<?>  addCoupon(@RequestBody CouponEntity coupon) {
        try {
            System.out.println("Coupon added at backend: " + coupon);
            CouponEntity addedCoupon = couponService.addCoupon(coupon);
            return ResponseEntity.ok(addedCoupon);
        } catch (DataIntegrityViolationException e) {
            System.out.println("catch" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon or CouponCode already exists.");
        }
    }

    @GetMapping("/getAllCoupon")
    public List<CouponEntity> getAllCoupon(){

        try {
          List<CouponEntity> coupon  =couponService.getAllCoupon();

            return coupon;
        } catch (IllegalArgumentException e) {
          List<CouponEntity> couponEntities = new ArrayList<>();
            return couponEntities;
        }
    }

    @GetMapping("/showAllCoupon")
    public List<CouponEntity> showCoupons(){
        try {
            List<CouponEntity> coupons  =couponService.getAllCoupon();
            return coupons.stream()
                    .filter(CouponEntity::isEnabled)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            List<CouponEntity> couponEntities = new ArrayList<>();
            return couponEntities;
        }
    }

    @PostMapping("/applyCoupon")
    public ResponseEntity<?> applyCoupon(@RequestBody CouponRequest coupon) {
        System.out.println("coupon : "+ coupon);
        double discountedTotalFare = applyCouponLogic(coupon.getSelectedCoupon().getMaxPrice(),coupon.getSelectedCoupon().getMinFare(),coupon.getSelectedCoupon().getOffPercent(), coupon.getTotalFare());

        return ResponseEntity.ok().body(discountedTotalFare);
    }

    private double applyCouponLogic(double maxPrice,int minFare,int  offPercent, double totalFare) {

        System.out.println("off percent : "+offPercent);
        System.out.println("totalFare : "+totalFare);
        System.out.println("Min Fare : "+minFare);
        System.out.println("Max price : "+maxPrice);
        if(minFare<=totalFare) {

            double discountedTotalFare = totalFare - (totalFare * offPercent / 100);
            if (discountedTotalFare < maxPrice) {
                System.out.println("discounted price :  " + discountedTotalFare);
                return discountedTotalFare;
            } else {
                System.out.println("else maxPrice applied");
                return totalFare - maxPrice;
            }
        }
        else {
            return totalFare;
        }
    }




    @PostMapping("/editCoupon/{couponId}")
    public ResponseEntity<CouponEntity>  editCoupon(@RequestBody CouponEntity coupon,@PathVariable String couponId){

        System.out.println("coupon  edit for edit "+couponId);
        System.out.println("coupon  edit for Data " + coupon);
        UUID uuid= UUID.fromString(couponId);
        System.out.println("Bus  edit UUID "+uuid);
        CouponEntity couponInfo= couponService.editCoupon(coupon,uuid);
        if (couponInfo!=null){
            LOGGER.info("Add bus Success ");
            return ResponseEntity.status(HttpStatus.CREATED).body(couponInfo);
        }else{
            LOGGER.info("Add bus Failed ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(couponInfo);
        }

    }

    @PostMapping("/addRating")
    public Rating addRating(@RequestParam int ratingValue, @RequestParam String bookingId, @RequestParam String busId) {
        System.out.println("RATING DATA ratingValue "+ratingValue);
        System.out.println("RATING DATA bookingId  "+bookingId);
        System.out.println("RATING DATA  busId  "+busId);
        Rating rating =busService.addRating(ratingValue,bookingId,busId);
        return rating;
    }

//    @GetMapping("/getRating/{busId}")
//    public  Rating getBusRatingByBusId(@PathVariable String busId){
//        Rating  rating= busService.getBusRatingByBusId(busId);
//     return  rating;
//    }

    @GetMapping("/getRating")
    public ResponseEntity<Rating> getBusRatingByBusId(@RequestParam String busId) {
        Rating rating = busService.getBusRatingByBusId(busId);
        if (rating != null) {
            return ResponseEntity.ok(rating);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
