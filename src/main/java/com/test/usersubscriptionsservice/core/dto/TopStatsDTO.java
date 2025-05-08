package com.test.usersubscriptionsservice.core.dto;


import java.util.List;

public record TopStatsDTO(List<SubscriptionStat> topSubscriptions) {
    public record SubscriptionStat(String serviceName, long userCount) {}
}
