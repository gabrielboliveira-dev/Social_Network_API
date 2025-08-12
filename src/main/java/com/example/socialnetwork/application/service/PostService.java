package com.example.socialnetwork.application.service;

import com.example.socialnetwork.entity.Post;
import com.example.socialnetwork.entity.User;
import com.example.socialnetwork.domain.repository.PostRepository;
import com.example.socialnetwork.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(Long userId, String content) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Post post = new Post(user, content);
            return postRepository.save(post);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void deletePost(Long id, Long userId) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getUser().getId().equals(userId)) {
                postRepository.deleteById(id);
            } else {
                throw new IllegalStateException("Not authorized to delete this post");
            }
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }
}