package com.test.usersubscriptionsservice.api.controller;

import com.test.usersubscriptionsservice.core.dto.SubscriptionAdditionDTO;
import com.test.usersubscriptionsservice.core.dto.SubscriptionDTO;
import com.test.usersubscriptionsservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<SubscriptionDTO> addSubscription(@PathVariable Long userId, @RequestBody SubscriptionAdditionDTO dto) {
        log.info("POST /api/users/{}/subscriptions - body: {}", userId, dto);
        SubscriptionDTO subscription = subscriptionService.addSubscriptionToUser(userId, dto);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId) {
        log.info("GET /api/users/{}/subscriptions", userId);
        List<SubscriptionDTO> subscriptions = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{userId}/subscriptions/page")
    public ResponseEntity<Page<SubscriptionDTO>> getUserSubscriptionsPaged(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/users/{}/subscriptions/page?page={}&size={}", userId, page, size);
        Page<SubscriptionDTO> subscriptions = subscriptionService.getUserSubscriptionsPaged(userId, page, size);
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{userId}/subscriptions/{sub_id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long userId, @PathVariable Long sub_id) {
        log.info("DELETE /api/users/{}/subscriptions/{}", userId, sub_id);
        subscriptionService.removeUserSubscription(userId, sub_id);
        return ResponseEntity.noContent().build();
    }
}
