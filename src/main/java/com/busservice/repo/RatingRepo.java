package com.busservice.repo;

import com.busservice.entity.BusRootAndTime;
import com.busservice.entity.Rating;
import com.ctc.wstx.shaded.msv_core.datatype.xsd.UnicodeUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepo extends JpaRepository<Rating , UUID> {

    Optional<Rating> findByBookingId(UUID bookingId);


    Rating findByBookingIdAndBusId(UUID bookingUuid, UUID busUuid);

    Rating findByBusId(UUID busUuid);



}
