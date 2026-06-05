package com.iexceed.appzillon.appstore.repository;

import com.iexceed.appzillon.appstore.entity.AppMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppMasterRepository extends JpaRepository<AppMasterEntity, String> {

    boolean existsByBundleId(String bundleId);

    List<AppMasterEntity> findAllByOrderByCreatedAtDesc();

    List<AppMasterEntity> findByAccessGroupOrderByCreatedAtDesc(String accessGroup);
}
