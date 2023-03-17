/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.models.Post;
import com.jbv.bfms.bookforumms.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Post> getAllPosts() {

        log.debug("Get All Posts in controller was called.");

        return postService.getAllPosts();
    }

    @GetMapping(POST_PATH_ID)
    public Post getPostById(@PathVariable("postId") UUID postId) {

        log.debug("Get Post By Id in controller was called.");

        return postService.getPostById(postId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(POST_PATH)
    public ResponseEntity createPost(@RequestBody Post post) {

        log.debug("Create Post in controller was called.");

        Post newPost = postService.createPost(post);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", (POST_PATH + newPost.getPostId().toString()));

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(POST_PATH_ID)
    public ResponseEntity editPost(@PathVariable("postId") UUID postId, @RequestBody Post post) {

        log.debug("Edit Post in controller was called.");

        postService.editPost(postId, post);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(POST_PATH_ID)
    public ResponseEntity patchPost(@PathVariable("postId") UUID postId, @RequestBody Post post) {

        log.debug("Patch Post in controller was called.");

        postService.patchPost(postId, post);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(POST_PATH_ID)
    public ResponseEntity deletePost(@PathVariable UUID postId) {

        log.debug("Delete Post in controller was called.");

        postService.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}