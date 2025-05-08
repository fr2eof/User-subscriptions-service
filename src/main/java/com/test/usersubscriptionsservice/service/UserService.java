package com.test.usersubscriptionsservice.service;

import com.test.usersubscriptionsservice.core.dto.UserCreateRequest;
import com.test.usersubscriptionsservice.core.dto.UserDTO;
import com.test.usersubscriptionsservice.core.dto.UserUpdateRequest;


public interface UserService {
    UserDTO createUser(UserCreateRequest request);

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
