package com.test.usersubscriptionsservice.core.dto;

import com.test.usersubscriptionsservice.core.model.SubscriptionServiceType;

import java.time.LocalDateTime;

public record SubscriptionDTO(
        Long id,
        SubscriptionServiceType serviceType,
        LocalDateTime startDate,
        LocalDateTime expireDate,
        Long userId
) {
}
