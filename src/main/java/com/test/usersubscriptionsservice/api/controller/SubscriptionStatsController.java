package com.test.usersubscriptionsservice.api.controller;

import com.test.usersubscriptionsservice.core.dto.TopStatsDTO;
import com.test.usersubscriptionsservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionStatsController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/top")
    public ResponseEntity<TopStatsDTO> top() {
        TopStatsDTO topSubscriptions = subscriptionService.getTopSubscriptions();
        return ResponseEntity.ok(topSubscriptions);
    }
}
