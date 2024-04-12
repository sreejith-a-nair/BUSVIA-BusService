package com.busservice.repo;

import com.busservice.entity.BusRootAndTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface RootAndTimeRepo extends JpaRepository<BusRootAndTime, UUID> {
    List<BusRootAndTime> findBySourceLocationAndDestinationLocationAndDepartureDate(String sourceLocation, String destinationLocation, Date departureDate);
    List<BusRootAndTime> findByBusId(UUID busId);

//    @Query("SELECT b FROM BusRootAndTime b WHERE " +
//            "TO_TIMESTAMP(b.departureTime, 'hh:mma') >= TO_TIMESTAMP(?1, 'hh:mma') " +
//            "AND TO_TIMESTAMP(b.departureTime, 'hh:mma') <= TO_TIMESTAMP(?2, 'hh:mma')")
//
    List<BusRootAndTime> findByDepartureTimeBetween(String startTime, String endTime);

    List<BusRootAndTime> findByRootType(String regular);

}
