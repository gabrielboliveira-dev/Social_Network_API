package com.example.socialnetwork.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommentResponse {
    private UUID id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
