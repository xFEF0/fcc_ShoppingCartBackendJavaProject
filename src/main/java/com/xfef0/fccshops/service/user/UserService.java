package com.xfef0.fccshops.service.user;

import com.xfef0.fccshops.exception.AlreadyExistsException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.User;
import com.xfef0.fccshops.repository.UserRepository;
import com.xfef0.fccshops.request.CreateUserRequest;
import com.xfef0.fccshops.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(createUserRequest -> {
                    User newUser = new User();
                    newUser.setFirstName(createUserRequest.getFirstName());
                    newUser.setLastName(createUserRequest.getLastName());
                    newUser.setEmail(createUserRequest.getEmail());
                    newUser.setPassword(createUserRequest.getPassword());
                    return userRepository.save(newUser);
                }).orElseThrow(() -> new AlreadyExistsException("Sorry, " + request.getEmail() + " already in use"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}
