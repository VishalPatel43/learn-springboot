package com.springboot.coding.prod_ready_features.services;

import com.springboot.coding.prod_ready_features.dto.PostDTO;
import com.springboot.coding.prod_ready_features.entities.PostEntity;
import com.springboot.coding.prod_ready_features.exceptions.ResourceNotFoundException;
import com.springboot.coding.prod_ready_features.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok an annotation to generate constructor for final fields
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(
                        postEntity -> modelMapper
                                .map(postEntity, PostDTO.class))
                .toList();
    }

    @Override
    public PostDTO createNewPost(PostDTO inputPost) {
        PostEntity postEntity = modelMapper.map(inputPost, PostEntity.class);
        return modelMapper
                .map(
                        postRepository.save(postEntity), PostDTO.class
                );
    }

    @Override
    public PostDTO getPostById(Long postId) {
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return modelMapper.map(postEntity, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO inputPost, Long postId) {
        PostEntity olderPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        inputPost.setPostId(postId);
        // source, destination
        modelMapper.map(inputPost, olderPost); // here we update the older post with the inputPost

        PostEntity savedPostEntity = postRepository.save(olderPost);
        return modelMapper.map(savedPostEntity, PostDTO.class);
    }
}
