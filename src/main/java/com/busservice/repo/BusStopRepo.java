package com.busservice.repo;

import com.busservice.entity.BusStopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusStopRepo extends JpaRepository<BusStopEntity , UUID> {

}
