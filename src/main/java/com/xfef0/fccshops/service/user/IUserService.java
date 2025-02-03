package com.xfef0.fccshops.service.user;

import com.xfef0.fccshops.dto.UserDTO;
import com.xfef0.fccshops.request.CreateUserRequest;
import com.xfef0.fccshops.request.UpdateUserRequest;

public interface IUserService {

    UserDTO getUserById(Long userId);
    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

}
