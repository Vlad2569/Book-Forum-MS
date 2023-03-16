/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.models.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts();

    Post getPostById(UUID postId);

    Post createPost(Post post);

    void editPost(UUID postId, Post post);

    void patchPost(UUID postId, Post post);

    void deletePost(UUID postId);
}