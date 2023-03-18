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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public Optional<PostDto> editPost(UUID postId, PostDto postDto) {

        AtomicReference<Optional<PostDto>> atomicReference = new AtomicReference<>();

        postRepository.findById(postId).ifPresentOrElse(foundPost -> {
            foundPost.setTitle(postDto.getTitle());
            foundPost.setBody(postDto.getBody());
            foundPost.setLastUpdate(LocalDateTime.now());
            atomicReference.set(Optional.of(postMapper.postToPostDto(postRepository.save(foundPost))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Optional<PostDto> patchPost(UUID postId, PostDto postDto) {

        AtomicReference<Optional<PostDto>> atomicReference = new AtomicReference<>();

        postRepository.findById(postId).ifPresentOrElse(foundPost -> {
            if (StringUtils.hasText(postDto.getTitle())) {
                foundPost.setTitle(postDto.getTitle());
            }
            if (StringUtils.hasText(postDto.getBody())) {
                foundPost.setBody(postDto.getBody());
            }
            atomicReference.set(Optional.of(postMapper.postToPostDto(postRepository.save(foundPost))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deletePost(UUID postId) {

        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }

        return false;
    }
}