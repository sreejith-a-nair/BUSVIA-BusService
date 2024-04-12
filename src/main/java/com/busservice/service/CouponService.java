package com.busservice.service;

import com.busservice.entity.CouponEntity;
import com.busservice.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CouponService {


    CouponEntity addCoupon(CouponEntity coupon);

//     CouponEntity editCoupon(CouponEntity coupon);

   List<CouponEntity> getAllCoupon();

    CouponEntity editCoupon(CouponEntity coupon, UUID uuid);

    void blockCoupon(UUID couponId);
    void unBlockCoupon(UUID couponId);



}
