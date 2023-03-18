/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.jbv.bfms.bookforumms.dtos.PostDto;
import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    public static final String POST_PATH = "/api/v1/post";
    public static final String POST_PATH_ID = POST_PATH + "/{postId}";

    private final PostService postService;

    @GetMapping(POST_PATH)
    public List<PostDto> getAllPosts() {

        log.debug("Get All Posts in controller was called.");

        return postService.getAllPosts();
    }

    @GetMapping(POST_PATH_ID)
    public PostDto getPostById(@PathVariable("postId") UUID postId) {

        log.debug("Get Post By Id in controller was called.");

        return postService.getPostById(postId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(POST_PATH)
    public ResponseEntity createPost(@Validated @RequestBody PostDto postDto) {

        log.debug("Create Post in controller was called.");

        PostDto newPostDto = postService.createPost(postDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", POST_PATH + "/" + newPostDto.getPostId().toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(POST_PATH_ID)
    public ResponseEntity editPost(@PathVariable("postId") UUID postId, @RequestBody PostDto postDto) {

        log.debug("Edit Post in controller was called.");

        if (postService.editPost(postId, postDto).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(POST_PATH_ID)
    public ResponseEntity patchPost(@PathVariable("postId") UUID postId, @RequestBody PostDto postDto) {

        log.debug("Patch Post in controller was called.");

        if (postService.patchPost(postId, postDto).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(POST_PATH_ID)
    public ResponseEntity deletePost(@PathVariable UUID postId) {

        log.debug("Delete Post in controller was called.");

        if (!postService.deletePost(postId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}