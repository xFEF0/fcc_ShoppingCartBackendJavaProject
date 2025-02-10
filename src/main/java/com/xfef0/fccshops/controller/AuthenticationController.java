package com.xfef0.fccshops.controller;

import com.xfef0.fccshops.request.LoginRequest;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.response.JwtResponse;
import com.xfef0.fccshops.security.jwt.JwtUtils;
import com.xfef0.fccshops.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwtToken);
            return ResponseEntity.ok(new ApiResponse("Login success", jwtResponse));
        } catch (AuthenticationException e) {
            return getExceptionResponseEntity(e, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return getExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static ResponseEntity<ApiResponse> getExceptionResponseEntity(Exception e, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse(e.getMessage(), null));
    }
}
