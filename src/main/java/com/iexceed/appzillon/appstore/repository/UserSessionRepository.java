package com.iexceed.appzillon.appstore.repository;

import com.iexceed.appzillon.appstore.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionEntity, String> {
	java.util.Optional<UserSessionEntity> findByRefreshToken(String refreshToken);
	long countByUserIdAndStatus(String userId, String status);
	java.util.List<UserSessionEntity> findByStatusAndExpireAtBefore(String status, java.time.Instant instant);
}
