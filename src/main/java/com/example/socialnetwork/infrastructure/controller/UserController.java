package com.example.socialnetwork.infrastructure.controller;

// MUDANÇA: Imports para os nossos novos casos de uso e DTOs.
import com.example.socialnetwork.application.usecase.user.FindUserByIdUseCase;
import com.example.socialnetwork.application.usecase.user.FindUserByUsernameUseCase;
import com.example.socialnetwork.application.usecase.user.FollowUserUseCase;
import com.example.socialnetwork.application.usecase.user.UnfollowUserUseCase;
import com.example.socialnetwork.application.usecase.user.UpdateUserProfileImageUseCase;
import com.example.socialnetwork.application.service.FileStorageService;
import com.example.socialnetwork.domain.entity.User;
import com.example.socialnetwork.infrastructure.dto.ImageUploadResponse;
import com.example.socialnetwork.infrastructure.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final UpdateUserProfileImageUseCase updateUserProfileImageUseCase;
    private final FollowUserUseCase followUserUseCase;
    private final UnfollowUserUseCase unfollowUserUseCase;

    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return findUserByIdUseCase.handle(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<ImageUploadResponse> uploadProfileImage(@RequestParam("file") MultipartFile file, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        try {
            String filename = fileStorageService.save(file);

            var command = new UpdateUserProfileImageUseCase.UpdateUserProfileImageCommand(currentUser.getId(), filename, currentUser);
            updateUserProfileImageUseCase.handle(command);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(filename)
                    .toUriString();

            ImageUploadResponse response = new ImageUploadResponse();
            response.setFileName(filename);
            response.setFileDownloadUri(fileDownloadUri);
            response.setFileType(file.getContentType());
            response.setSize(file.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable UUID followingId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        try {
            var command = new FollowUserUseCase.FollowUserCommand(currentUser.getId(), followingId);
            followUserUseCase.handle(command);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @DeleteMapping("/unfollow/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable UUID followingId, Authentication authentication) {
        User currentUser = findUserOrThrow(authentication);

        try {
            var command = new UnfollowUserUseCase.UnfollowUserCommand(currentUser.getId(), followingId);
            unfollowUserUseCase.handle(command);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private User findUserOrThrow(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findUserByUsernameUseCase.handle(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }

    private UserResponse convertToDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}