package com.test.usersubscriptionsservice.service;

import com.test.usersubscriptionsservice.core.dto.SubscriptionAdditionDTO;
import com.test.usersubscriptionsservice.core.dto.SubscriptionDTO;
import com.test.usersubscriptionsservice.core.dto.TopStatsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDTO addSubscriptionToUser(Long userId, SubscriptionAdditionDTO dto);

    void removeUserSubscription(Long userId, Long subId);

    List<SubscriptionDTO> getUserSubscriptions(Long userId);

    Page<SubscriptionDTO> getUserSubscriptionsPaged(Long userId, int page, int size);

    TopStatsDTO getTopSubscriptions();
}
