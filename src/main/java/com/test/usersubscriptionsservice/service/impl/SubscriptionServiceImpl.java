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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDTO addSubscriptionToUser(Long userId, SubscriptionAdditionDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setServiceType(SubscriptionServiceType.valueOf(dto.serviceType()));
        subscription.setStartDate(dto.startDate());
        subscription.setExpireDate(dto.expireDate());

        Subscription saved = subscriptionRepository.save(subscription);
        return mapToDTO(saved);
    }

    @Override
    public void removeUserSubscription(Long userId, Long subId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with ID: " + subId));

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Subscription does not belong to the user.");
        }

        subscriptionRepository.delete(subscription);
    }


    @Override
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        return subscriptionRepository.findAllByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Page<SubscriptionDTO> getUserSubscriptionsPaged(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<Subscription> pageResult = subscriptionRepository.findAllByUserId(userId, pageable);

        return pageResult.map(this::mapToDTO);
    }

    @Override
    public TopStatsDTO getTopSubscriptions() {
        List<Object[]> rawResults = subscriptionRepository.findTopSubscriptionsRaw();

        List<TopStatsDTO.SubscriptionStat> stats = rawResults.stream()
                .limit(3)
                .map(row -> new TopStatsDTO.SubscriptionStat(
                        row[0].toString(),
                        (long) row[1]))
                .toList();

        return new TopStatsDTO(stats);
    }


    private SubscriptionDTO mapToDTO(Subscription sub) {
        return new SubscriptionDTO(
                sub.getId(),
                sub.getServiceType(),
                sub.getStartDate(),
                sub.getExpireDate(),
                sub.getUser().getId());
    }
}
