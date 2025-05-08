package com.test.usersubscriptionsservice.service.impl;

import com.test.usersubscriptionsservice.core.dto.UserCreateRequest;
import com.test.usersubscriptionsservice.core.dto.UserDTO;
import com.test.usersubscriptionsservice.core.dto.UserUpdateRequest;
import com.test.usersubscriptionsservice.core.exception.UserNotFoundException;
import com.test.usersubscriptionsservice.core.model.User;
import com.test.usersubscriptionsservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldSaveAndReturnDTO() {
        UserCreateRequest request = new UserCreateRequest("user", "email@mail.com", "pass");

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("email@mail.com");

        when(userRepository.save(any())).thenReturn(user);

        UserDTO dto = userService.createUser(request);
        assertEquals("user", dto.username());
    }

    @Test
    void getUserById_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("e");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO dto = userService.getUserById(1L);
        assertEquals("user", dto.username());
    }

    @Test
    void getUserById_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_shouldUpdateAndReturnDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("old");
        user.setEmail("old@mail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserUpdateRequest req = new UserUpdateRequest("newname", "newpass");

        UserDTO dto = userService.updateUser(1L, req);
        assertEquals("newname", dto.username());
    }

    @Test
    void updateUser_userNotFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1L, new UserUpdateRequest("u", "p")));
    }

    @Test
    void deleteUser_shouldCallRepository() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}

