/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.models.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    private final Map<UUID, Post> postMap;

    public PostServiceImpl() {
        this.postMap = new HashMap<>();

        Post post1 = Post.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("First Forum Post")
                .body("Explain the idea and specifics of the REST API" +
                        " + Client UI architecture for web applications.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("Second Forum Post")
                .body("Explain the MVC design pattern and " +
                        "how it can be combined with a layered achitecture.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        Post post3 = Post.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("Third Forum Post")
                .body("Understand the MVC design pattern and " +
                        "how it fits in the layered architecture.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        postMap.put(post1.getPostId(), post1);
        postMap.put(post2.getPostId(), post2);
        postMap.put(post3.getPostId(), post3);
    }

    @Override
    public List<Post> getAllPosts() {

        log.debug("Get All Posts in service was called.");

        return new ArrayList<>(postMap.values());
    }

    @Override
    public Optional<Post> getPostById(UUID postId) {

        log.debug("Get Post By Id in service was called. Id: " + postId.toString());

        return Optional.of(postMap.get(postId));
    }

    @Override
    public Post createPost(Post post) {

        Post newPost = Post.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        postMap.put(newPost.getPostId(), newPost);

        log.debug("Create Post in service was called. Id: " + newPost.getPostId().toString());

        return newPost;
    }

    @Override
    public void editPost(UUID postId, Post post) {

        Post postToEdit = postMap.get(postId);

        postToEdit.setTitle(post.getTitle());
        postToEdit.setBody(post.getBody());
        postToEdit.setLastUpdate(LocalDateTime.now());

        log.debug("Edit Post in service was called. Id: " + postToEdit.getPostId().toString());
    }

    @Override
    public void patchPost(UUID postId, Post post) {

        Post postToPatch = postMap.get(postId);

        if (StringUtils.hasText(post.getTitle())) {
            postToPatch.setTitle(post.getTitle());
        }

        if (StringUtils.hasText(post.getBody())) {
            postToPatch.setBody(post.getBody());
        }

        postToPatch.setLastUpdate(LocalDateTime.now());

        log.debug("Patch Post in service was called. Id: " + postToPatch.getPostId().toString());
    }

    @Override
    public void deletePost(UUID postId) {

        postMap.remove(postId);

        log.debug("Delete Post in service was called. Id: " + postId.toString());
    }
}