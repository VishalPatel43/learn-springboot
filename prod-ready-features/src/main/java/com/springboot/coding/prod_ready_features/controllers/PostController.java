package com.springboot.coding.prod_ready_features.controllers;

import com.springboot.coding.prod_ready_features.dto.PostDTO;
import com.springboot.coding.prod_ready_features.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    //    Swagger: https://springdoc.org/
    //    http://localhost:9000/v3/api-docs
    //    http://localhost:9000/swagger-ui.html
    private final PostService postService;

    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping(path = "/{postId}")
    public PostDTO getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost) {
        return postService.createNewPost(inputPost);
    }

    @PutMapping(path = "/{postId}")
    public PostDTO updatePost(@RequestBody PostDTO inputPost, @PathVariable Long postId) {
        return postService.updatePost(inputPost, postId);
    }

}
