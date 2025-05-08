package com.test.usersubscriptionsservice.repository;

import com.test.usersubscriptionsservice.core.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserId(Long userId);

    Page<Subscription> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT s.serviceType AS serviceName, COUNT(s.user) AS userCount " +
            "FROM Subscription s GROUP BY s.serviceType ORDER BY COUNT(s.user) DESC")
    List<Object[]> findTopSubscriptionsRaw();

}
