package com.busservice.repo;

import com.busservice.entity.BusEntity;
import com.busservice.model.FeatureRequestForSearch;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BusRepo extends JpaRepository<BusEntity, UUID> {

    List<BusEntity> findAllByEmail(String email);

    List<BusEntity> findByCategory(String featureRequestForSearch);

}
