package com.example.socialnetwork.infrastructure.controller;

import com.example.socialnetwork.application.service.UserServiceImpl;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.dto.LoginRequest;
import com.example.socialnetwork.infrastructure.dto.LoginResponse;
import com.example.socialnetwork.infrastructure.dto.UserRegistrationRequest;
import com.example.socialnetwork.infrastructure.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserServiceImpl userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User newUser = User.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .build();
            User registeredUser = userService.registerUser(newUser);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}