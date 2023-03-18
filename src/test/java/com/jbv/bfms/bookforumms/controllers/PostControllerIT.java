/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.jbv.bfms.bookforumms.dtos.PostDto;
import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.mappers.PostMapper;
import com.jbv.bfms.bookforumms.models.Post;
import com.jbv.bfms.bookforumms.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostControllerIT {

    @Autowired
    PostController postController;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostMapper postMapper;

    @Test
    void testGetAllPosts() {
        List<PostDto> postDtos = postController.getAllPosts();

        assertThat(postDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        postRepository.deleteAll();
        List<PostDto> postDtos = postController.getAllPosts();

        assertThat(postDtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById() {

        Post post = postRepository.findAll().get(0);

        PostDto postDto = postController.getPostById(post.getPostId());

        assertThat(postDto).isNotNull();
    }

    @Test
    void testNotFound() {

        assertThrows(NotFoundException.class, () -> {
            postController.getPostById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testCreatePost() {

        PostDto postDto = PostDto.builder()
                .title("New Title")
                .build();

        ResponseEntity responseEntity = postController.createPost(postDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[]  locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Post post = postRepository.findById(savedUUID).get();
        assertThat(post).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testUpdatePost() {

        Post testPost = postRepository.findAll().get(0);
        PostDto postDto = postMapper.postToPostDto(testPost);
        postDto.setPostId(null);
        postDto.setVersion(null);
        final String postTitle = "UPDATED";
        postDto.setTitle(postTitle);

        ResponseEntity responseEntity = postController.editPost(testPost.getPostId(), postDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Post editedPost = postRepository.findById(testPost.getPostId()).get();
        assertThat(editedPost.getTitle()).isEqualTo(postTitle);
    }

    @Test
    void testUpdateNotFound() {

        assertThrows(NotFoundException.class, () -> {
            postController.editPost(UUID.randomUUID(), PostDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeletePost() {

        Post testPost = postRepository.findAll().get(0);

        ResponseEntity responseEntity = postController.deletePost(testPost.getPostId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(postRepository.findById(testPost.getPostId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() {

        assertThrows(NotFoundException.class, () -> {
            postController.deletePost(UUID.randomUUID());
        });
    }
}