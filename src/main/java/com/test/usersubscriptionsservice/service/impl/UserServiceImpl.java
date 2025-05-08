package com.test.usersubscriptionsservice.service.impl;

import com.test.usersubscriptionsservice.core.dto.UserCreateRequest;
import com.test.usersubscriptionsservice.core.dto.UserDTO;
import com.test.usersubscriptionsservice.core.dto.UserUpdateRequest;
import com.test.usersubscriptionsservice.core.exception.UserNotFoundException;
import com.test.usersubscriptionsservice.core.model.User;
import com.test.usersubscriptionsservice.repository.UserRepository;
import com.test.usersubscriptionsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateRequest userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (userDTO.newUsername() != null) {
            user.setEmail(userDTO.newUsername());
        }
        if (userDTO.newPassword() != null) {
            user.setPassword(userDTO.newPassword());
        }

        return mapToDTO(userRepository.save(user));
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
