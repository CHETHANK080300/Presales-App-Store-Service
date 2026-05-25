package com.iexceed.appzillon.appstore.repository;

import com.iexceed.appzillon.appstore.entity.ActiveUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveUserRepository extends JpaRepository<ActiveUserEntity, String> {
    List<ActiveUserEntity> findByRole(String role);
    long countByRole(String role);
}
