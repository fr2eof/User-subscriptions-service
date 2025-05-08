package com.test.usersubscriptionsservice.service.impl;

import com.test.usersubscriptionsservice.core.dto.SubscriptionAdditionDTO;
import com.test.usersubscriptionsservice.core.dto.SubscriptionDTO;
import com.test.usersubscriptionsservice.core.dto.TopStatsDTO;
import com.test.usersubscriptionsservice.core.exception.UserNotFoundException;
import com.test.usersubscriptionsservice.core.model.Subscription;
import com.test.usersubscriptionsservice.core.model.SubscriptionServiceType;
import com.test.usersubscriptionsservice.core.model.User;
import com.test.usersubscriptionsservice.repository.SubscriptionRepository;
import com.test.usersubscriptionsservice.repository.UserRepository;
import com.test.usersubscriptionsservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubscriptionDTO addSubscriptionToUser(Long userId, SubscriptionAdditionDTO dto) {
        log.info("Adding subscription for userId={}, serviceType={}", userId, dto.serviceType());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setServiceType(SubscriptionServiceType.valueOf(dto.serviceType()));
        subscription.setStartDate(dto.startDate());
        subscription.setExpireDate(dto.expireDate());

        Subscription saved = subscriptionRepository.save(subscription);
        log.debug("Subscription saved with ID={}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void removeUserSubscription(Long userId, Long subId) {
        log.info("Removing subscription ID={} for userId={}", subId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> {
                    log.warn("Subscription not found with ID: {}", subId);
                    return new IllegalArgumentException("Subscription not found with ID: " + subId);
                });

        if (!subscription.getUser().getId().equals(user.getId())) {
            log.warn("Subscription ID={} does not belong to userId={}", subId, userId);
            throw new IllegalArgumentException("Subscription does not belong to the user.");
        }

        subscriptionRepository.delete(subscription);
        log.debug("Subscription ID={} deleted", subId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        log.info("Fetching all subscriptions for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User not found with ID: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        List<SubscriptionDTO> dtos = subscriptionRepository.findAllByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
        log.debug("Found {} subscriptions", dtos.size());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionDTO> getUserSubscriptionsPaged(Long userId, int page, int size) {
        log.info("Fetching paged subscriptions for userId={}, page={}, size={}", userId, page, size);
        if (!userRepository.existsById(userId)) {
            log.warn("User not found with ID: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<SubscriptionDTO> result = subscriptionRepository.findAllByUserId(userId, pageable).map(this::mapToDTO);
        log.debug("Found {} subscriptions in page", result.getNumberOfElements());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public TopStatsDTO getTopSubscriptions() {
        log.info("Fetching top subscriptions");
        List<Object[]> rawResults = subscriptionRepository.findTopSubscriptionsRaw();

        List<TopStatsDTO.SubscriptionStat> stats = rawResults.stream()
                .limit(3)
                .map(row -> new TopStatsDTO.SubscriptionStat(row[0].toString(), (long) row[1]))
                .toList();

        log.debug("Top subscriptions: {}", stats);
        return new TopStatsDTO(stats);
    }

    private SubscriptionDTO mapToDTO(Subscription sub) {
        return new SubscriptionDTO(sub.getId(), sub.getServiceType(), sub.getStartDate(), sub.getExpireDate(), sub.getUser().getId());
    }
}
