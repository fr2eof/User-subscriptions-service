package com.test.usersubscriptionsservice.core.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SubscriptionAdditionDTO(
        @NotBlank(message = "Service type must not be blank")
        String serviceType,

        @NotNull(message = "Start date must not be null")
        LocalDateTime startDate,

        @NotNull(message = "Expire date must not be null")
        @Future(message = "Expire date must be in the future")
        LocalDateTime expireDate
) {

}
