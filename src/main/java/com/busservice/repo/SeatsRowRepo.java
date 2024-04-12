package com.busservice.repo;

import com.busservice.entity.SeatsRows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SeatsRowRepo extends JpaRepository<SeatsRows, UUID> {


}
