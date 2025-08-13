package com.example.socialnetwork.infrastructure.dto;

import com.example.socialnetwork.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private String content;
    private User author;
    private LocalDateTime createdAt;
}