package com.springboot.coding.securityApplication.utils;

import com.springboot.coding.securityApplication.dto.PostDTO;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.PostService;
import com.springboot.coding.securityApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Service
@Component
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;
    private final UserService userService;

    public boolean isOwnerOfPost(Long postId) {
        /*
        * Get current user from SecurityContextHolder

        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) authentication1.getPrincipal();

         */
        User user = userService.getCurrentUser();

        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getUserId().equals(user.getUserId());

    }
}
