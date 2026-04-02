package com.example.socialnetwork.infrastructure.dto;

import com.example.socialnetwork.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class PostResponse {
    private UUID id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<CommentResponse> comments = new ArrayList<>();
}