package com.busservice.repo;

import com.busservice.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponRepo extends JpaRepository<CouponEntity , UUID> {


}
