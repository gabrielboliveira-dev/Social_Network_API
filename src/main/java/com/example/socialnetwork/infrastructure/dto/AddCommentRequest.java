package com.example.socialnetwork.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {

    @NotBlank(message = "Comment content cannot be blank")
    private String content;
}