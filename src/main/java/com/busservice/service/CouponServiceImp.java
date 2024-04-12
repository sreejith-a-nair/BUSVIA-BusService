package com.busservice.service;

import com.busservice.entity.BusEntity;
import com.busservice.entity.CouponEntity;
import com.busservice.entity.WalletEntity;
import com.busservice.model.BusBlockRequest;
import com.busservice.repo.CouponRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponServiceImp implements  CouponService{

    @Autowired
    CouponRepo couponRepo;

    @Override
    public CouponEntity addCoupon(CouponEntity coupon) {
        if(coupon!=null){
        coupon.setEnabled(true);
        CouponEntity couponInfo=   couponRepo.save(coupon);
        System.out.println("coupon added :  "+ couponInfo);
        return couponInfo;
        }
        CouponEntity couponInfo = new CouponEntity();
        return couponInfo;
    }

//    @Override
//    public CouponEntity editCoupon(CouponEntity coupon) {
//        UUID couponId = coupon.getUuid();
//          CouponEntity couponData = couponRepo.findById(couponId).orElseThrow(()-> new RuntimeException( "couponData not found "));
//            couponData.setCouponName(coupon.getCouponName());
//            couponData.setCouponCode(coupon.getCouponCode());
//            couponData.setCount(coupon.getCount());
//            couponData.setMaxPrice(coupon.getMaxPrice());
//            couponData.setExpiryDate(coupon.getExpiryDate());
//            couponData.setOffPercent(coupon.getOffPercent());
//        System.out.println("Edited coupon data : "+ couponData);
//        return couponData;
//    }

    @Override
    public List<CouponEntity> getAllCoupon() {
      List<CouponEntity>  coupon= couponRepo.findAll();
        return coupon;
    }

    @Override
    public CouponEntity editCoupon(CouponEntity coupon, UUID uuid) {
        UUID couponId = uuid;
        System.out.println("Coupon id for edit : "+couponId);
        CouponEntity couponData = couponRepo.findById(couponId).orElseThrow(()-> new RuntimeException( "couponData not found "));
        couponData.setCouponName(coupon.getCouponName());
        couponData.setCouponCode(coupon.getCouponCode());
        couponData.setCount(coupon.getCount());
        couponData.setMaxPrice(coupon.getMaxPrice());
        couponData.setExpiryDate(coupon.getExpiryDate());
        couponData.setOffPercent(coupon.getOffPercent());
        couponData.setMinFare(coupon.getMinFare());
        CouponEntity couponInfo =couponRepo.save(couponData);
        System.out.println("Edited coupon data : "+ couponData);
        return couponInfo;
    }

    @Override
    public void blockCoupon(UUID couponId) {
        Optional<CouponEntity> coupon = couponRepo.findById(couponId);
        if (coupon.isPresent()) {
            CouponEntity couponInfo = coupon.get();
            couponInfo.setEnabled(false);
            CouponEntity couponUpdate   =   couponRepo.save(couponInfo);

            System.out.println("Response from Coupon block service: " + couponUpdate);

        } else {
            throw new EntityNotFoundException("Bus not found with ID: " + couponId);
        }
    }

    @Override
    public void unBlockCoupon(UUID couponId) {
        Optional<CouponEntity> coupon = couponRepo.findById(couponId);
        if (coupon.isPresent()) {
            CouponEntity couponInfo = coupon.get();
            couponInfo.setEnabled(true);
            CouponEntity couponUpdate   =   couponRepo.save(couponInfo);

            System.out.println("Response from Coupon unblock  service: " + couponUpdate);

        } else {
            throw new EntityNotFoundException("Coupon not found with ID: " + couponId);
        }
    }



}
