package com.springboot.coding.securityApplication.controllers;

import com.springboot.coding.securityApplication.dto.PostDTO;
import com.springboot.coding.securityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_CREATOR"})
    // --> We can use this anywhere but thumb rule is to use it on the Controller
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
//    @PreAuthorize("hasRole('ADMIN')") // --> we can pass the expression
//    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')") // --> we can pass the expression
//    @PreAuthorize("hasAnyAuthority('POST_VIEW')")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR') OR hasAnyAuthority('POST_VIEW')")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)") // we can call the method from another service also
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
    @PostMapping
    public ResponseEntity<PostDTO> createNewPost(@RequestBody PostDTO inputPost) {
        return ResponseEntity.ok(postService.createNewPost(inputPost));
    }

    @GetMapping("/free-content")
    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan('FREE')")
    public String getFreeContent() {
        return "This is FREE content accessible to all users.";
    }

    @GetMapping("/basic-content")
    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan('BASIC')")
    public String getBasicContent() {
        return "This is BASIC content accessible to Basic and Premium plan users.";
    }

    @GetMapping("/premium-content")
    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan('PREMIUM')")
    public String getPremiumContent() {
        return "This is PREMIUM content accessible only to Premium users.";
    }
}
