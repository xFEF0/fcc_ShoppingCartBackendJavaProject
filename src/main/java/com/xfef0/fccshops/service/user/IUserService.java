package com.xfef0.fccshops.service.user;

import com.xfef0.fccshops.dto.UserDTO;
import com.xfef0.fccshops.model.User;
import com.xfef0.fccshops.request.CreateUserRequest;
import com.xfef0.fccshops.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    UserDTO getUserDTOById(Long userId);
    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
}
