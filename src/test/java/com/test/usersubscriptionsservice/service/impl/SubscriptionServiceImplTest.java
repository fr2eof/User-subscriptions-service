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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void addSubscriptionToUser_shouldSaveAndReturnSubscription() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        SubscriptionAdditionDTO dto = new SubscriptionAdditionDTO(
                "NETFLIX",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );

        Subscription sub = new Subscription();
        sub.setId(42L);
        sub.setServiceType(SubscriptionServiceType.NETFLIX);
        sub.setStartDate(dto.startDate());
        sub.setExpireDate(dto.expireDate());
        sub.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.save(any())).thenReturn(sub);

        SubscriptionDTO result = subscriptionService.addSubscriptionToUser(userId, dto);

        assertEquals(42L, result.id());
        assertEquals(SubscriptionServiceType.NETFLIX, result.serviceType());
    }

    @Test
    void addSubscriptionToUser_userNotFound_shouldThrow() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> subscriptionService.addSubscriptionToUser(1L, mock(SubscriptionAdditionDTO.class)));
    }

    @Test
    void removeUserSubscription_shouldDeleteIfExists() {
        Long userId = 1L;
        Long subId = 2L;

        User user = new User();
        user.setId(userId);
        Subscription sub = new Subscription();
        sub.setId(subId);
        sub.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(subId)).thenReturn(Optional.of(sub));

        assertDoesNotThrow(() -> subscriptionService.removeUserSubscription(userId, subId));
        verify(subscriptionRepository).delete(sub);
    }

    @Test
    void removeUserSubscription_wrongUser_shouldThrow() {
        User user = new User();
        user.setId(1L);
        User another = new User();
        another.setId(2L);
        Subscription sub = new Subscription();
        sub.setUser(another);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        assertThrows(IllegalArgumentException.class,
                () -> subscriptionService.removeUserSubscription(1L, 99L));
    }

    @Test
    void getUserSubscriptions_shouldReturnMappedList() {
        User user = new User();
        user.setId(1L);
        Subscription s = new Subscription();
        s.setId(10L);
        s.setUser(user);
        s.setServiceType(SubscriptionServiceType.YOUTUBE_PREMIUM);
        s.setStartDate(LocalDateTime.now());
        s.setExpireDate(LocalDateTime.now().plusDays(10));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of(s));

        List<SubscriptionDTO> result = subscriptionService.getUserSubscriptions(1L);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).id());
    }

    @Test
    void getUserSubscriptions_userNotFound_shouldThrow() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class,
                () -> subscriptionService.getUserSubscriptions(1L));
    }

    @Test
    void getTopSubscriptions_shouldReturnStats() {
        when(subscriptionRepository.findTopSubscriptionsRaw()).thenReturn(List.of(
                new Object[]{"NETFLIX", 5L},
                new Object[]{"YOUTUBE", 3L}
        ));

        TopStatsDTO result = subscriptionService.getTopSubscriptions();

        assertEquals(2, result.topSubscriptions().size());
        assertEquals("NETFLIX", result.topSubscriptions().get(0).serviceName());
        assertEquals(5L, result.topSubscriptions().get(0).userCount());
    }
}

