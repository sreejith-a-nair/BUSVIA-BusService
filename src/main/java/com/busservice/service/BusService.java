package com.busservice.service;

import com.busservice.entity.*;
import com.busservice.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BusService {

    BusEntity addBus(BusEntity busData);

    List<BusEntity> getALlBus();



    List<BusEntity> getALlBusByMail(String mail);

    BusEntity updateBus(BusEntity busData, UUID uuid);

    BusRootAndTime updateRootAndTime(BusRootAndTime busRootAndTime, UUID uuid);

    void deleteBus(UUID busId);

    String saveUser(UserRequest userRequest);

    BusRootAndTime addBusRootAndTime(BusRootRequest busRoot);

    BusStopEntity addBusStop(BusStopRequest busStopRequest);

    List<BusEntity> getSearchedBus(SearchRequest searchRequest);

    List<BusSearchData> getSearchedBuses(SearchRequest searchRequest);
    List<BusSearchData> filterByFeature(FeatureRequestForSearch featureRequestForSearch);
//    BusEntity  findBusById(UUID busUuid);
    BusResponse  findBusById(UUID busUuid);

    List<BusSearchData> getBusRootAndTimeBetweenTimePeriod(String startTime, String endTime);

    List<SeatsRows> addSeatRows(Test seatRows);

    BusRootAndTime findBusRouteByBusId(UUID busUuid);

    void unblockBus(UUID userUuid);

    void blockBus(UUID userUuid);



    void updateAvailableSeatAfterBooking(UUID busUuid, int bookedSeatCount);

    void updateAvailableSeatAfterCancel(UUID busUuid, int canceledSeatCount);


    Rating addRating(int ratingValue, String bookingId, String busId);

    Rating getBusRatingByBusId(String busId);

}


