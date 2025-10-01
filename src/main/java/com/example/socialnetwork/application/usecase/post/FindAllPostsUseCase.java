package com.example.socialnetwork.application.usecase.post;

import com.example.socialnetwork.domain.entity.Post;
import java.util.List;

public interface FindAllPostsUseCase {

    List<Post> handle();
}