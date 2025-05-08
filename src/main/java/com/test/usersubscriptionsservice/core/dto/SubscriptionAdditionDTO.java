package com.test.usersubscriptionsservice.core.dto;


import java.time.LocalDateTime;

public record SubscriptionAdditionDTO(
        String serviceType,
        LocalDateTime startDate,
        LocalDateTime expireDate
) {
}
