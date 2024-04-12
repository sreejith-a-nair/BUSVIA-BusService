package com.busservice.model;

import com.busservice.entity.CouponEntity;
import lombok.Data;

@Data
public class CouponRequest {

    private CouponEntity selectedCoupon;
    private double totalFare;

}
