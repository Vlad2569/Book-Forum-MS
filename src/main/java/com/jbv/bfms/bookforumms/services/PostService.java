/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.PostDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    List<PostDto> getAllPosts();

    Optional<PostDto> getPostById(UUID postId);

    PostDto createPost(PostDto postDto);

    Optional<PostDto> editPost(UUID postId, PostDto postDto);

    Optional<PostDto> patchPost(UUID postId, PostDto postDto);

    Boolean deletePost(UUID postId);
}