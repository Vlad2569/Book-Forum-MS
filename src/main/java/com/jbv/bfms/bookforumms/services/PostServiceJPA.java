/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.PostDto;
import com.jbv.bfms.bookforumms.mappers.PostMapper;
import com.jbv.bfms.bookforumms.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class PostServiceJPA implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::postToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PostDto> getPostById(UUID postId) {
        return Optional.ofNullable(postMapper.postToPostDto(postRepository.findById(postId)
                .orElse(null)));
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        return postMapper.postToPostDto(postRepository.save(postMapper.postDtoToPost(postDto)));
    }

    @Override
    public void editPost(UUID postId, PostDto postDto) {

    }

    @Override
    public void patchPost(UUID postId, PostDto postDto) {

    }

    @Override
    public void deletePost(UUID postId) {

    }
}