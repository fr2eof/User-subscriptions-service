package com.test.usersubscriptionsservice.core.dto;

public record UserUpdateRequest(
        String newUsername,
        String newPassword
) {
}
