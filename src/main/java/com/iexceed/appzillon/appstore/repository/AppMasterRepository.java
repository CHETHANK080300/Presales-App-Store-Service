package com.iexceed.appzillon.appstore.repository;

import com.iexceed.appzillon.appstore.entity.AppMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppMasterRepository extends JpaRepository<AppMasterEntity, String> {

    boolean existsByBundleId(String bundleId);
}