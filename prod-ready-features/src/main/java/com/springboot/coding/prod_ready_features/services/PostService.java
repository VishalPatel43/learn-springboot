package com.springboot.coding.prod_ready_features.services;

import com.springboot.coding.prod_ready_features.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);
}
