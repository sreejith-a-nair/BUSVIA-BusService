package com.busservice.repo;

import com.busservice.entity.OperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<OperatorEntity, UUID> {

}
