package com.xfef0.fccshops.controller;

import com.xfef0.fccshops.dto.UserDTO;
import com.xfef0.fccshops.exception.AlreadyExistsException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.request.CreateUserRequest;
import com.xfef0.fccshops.request.UpdateUserRequest;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Sucess", user));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            UserDTO userDTO = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("User created", userDTO));
        } catch (AlreadyExistsException e) {
            return getExceptionResponseEntity(e, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        try {
            UserDTO userDTO = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse("User updated", userDTO));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted", null));
        } catch (ResourceNotFoundException e) {
            return getExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ResponseEntity<ApiResponse> getExceptionResponseEntity(Exception e, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse(e.getMessage(), null));
    }

}
