package com.test.usersubscriptionsservice.api.controller;

import com.test.usersubscriptionsservice.core.dto.TopStatsDTO;
import com.test.usersubscriptionsservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionStatsController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/top")
    public ResponseEntity<TopStatsDTO> top() {
        log.info("GET /api/subscriptions/top");
        TopStatsDTO topSubscriptions = subscriptionService.getTopSubscriptions();
        return ResponseEntity.ok(topSubscriptions);
    }
}
