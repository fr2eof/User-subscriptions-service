package com.test.usersubscriptionsservice.api.controller;

import com.test.usersubscriptionsservice.core.dto.SubscriptionAdditionDTO;
import com.test.usersubscriptionsservice.core.dto.SubscriptionDTO;
import com.test.usersubscriptionsservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<SubscriptionDTO> addSubscription(@PathVariable Long userId, @RequestBody SubscriptionAdditionDTO dto) {
        SubscriptionDTO subscription = subscriptionService.addSubscriptionToUser(userId, dto);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId) {
        List<SubscriptionDTO> subscriptions = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping("/{userId}/subscriptions/{sub_id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long userId, @PathVariable Long sub_id) {
        subscriptionService.removeUserSubscription(userId, sub_id);
        return ResponseEntity.noContent().build();
    }
}

