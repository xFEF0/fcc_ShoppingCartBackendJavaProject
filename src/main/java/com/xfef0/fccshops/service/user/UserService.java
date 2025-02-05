package com.xfef0.fccshops.service.user;

import com.xfef0.fccshops.dto.UserDTO;
import com.xfef0.fccshops.exception.AlreadyExistsException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.User;
import com.xfef0.fccshops.repository.UserRepository;
import com.xfef0.fccshops.request.CreateUserRequest;
import com.xfef0.fccshops.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    public static final String USER_NOT_FOUND = "User not found.";
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public UserDTO getUserDTOById(Long userId) {
        return convertToDTO(getUserById(userId));
    }

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(this::createUserInDB)
                .map(this::convertToDTO)
                .orElseThrow(() -> new AlreadyExistsException("Sorry, " + request.getEmail() + " already in use"));
    }

    @Override
    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(userFound -> updateUserFirstAndLastNames(request, userFound))
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    private User createUserInDB(CreateUserRequest createUserRequest) {
        User newUser = new User();
        newUser.setFirstName(createUserRequest.getFirstName());
        newUser.setLastName(createUserRequest.getLastName());
        newUser.setEmail(createUserRequest.getEmail());
        newUser.setPassword(createUserRequest.getPassword());
        return userRepository.save(newUser);
    }

    private User updateUserFirstAndLastNames(UpdateUserRequest request, User userFound) {
        userFound.setFirstName(request.getFirstName());
        userFound.setLastName(request.getLastName());
        return userRepository.save(userFound);
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
