package com.test.usersubscriptionsservice.service.impl;

import com.test.usersubscriptionsservice.core.dto.UserCreateRequest;
import com.test.usersubscriptionsservice.core.dto.UserDTO;
import com.test.usersubscriptionsservice.core.dto.UserUpdateRequest;
import com.test.usersubscriptionsservice.core.exception.UserNotFoundException;
import com.test.usersubscriptionsservice.core.model.User;
import com.test.usersubscriptionsservice.repository.UserRepository;
import com.test.usersubscriptionsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        log.info("Creating user with username={}", request.username());
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        user = userRepository.save(user);
        log.debug("User created with ID={}", user.getId());
        return mapToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Retrieving user with ID={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateRequest userDTO) {
        log.info("Updating user with ID={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });

        if (userDTO.newUsername() != null) {
            log.debug("Updating username to {}", userDTO.newUsername());
            user.setUsername(userDTO.newUsername());
        }
        if (userDTO.newPassword() != null) {
            log.debug("Updating password for user ID={}", id);
            user.setPassword(userDTO.newPassword());
        }

        User saved = userRepository.save(user);
        log.debug("User with ID={} updated", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID={}", id);
        userRepository.deleteById(id);
        log.debug("User with ID={} deleted", id);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
