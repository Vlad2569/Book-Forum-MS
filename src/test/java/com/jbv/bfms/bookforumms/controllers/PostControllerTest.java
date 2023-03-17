/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.models.Post;
import com.jbv.bfms.bookforumms.services.PostService;
import com.jbv.bfms.bookforumms.services.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    PostServiceImpl postServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    @Captor
    ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

    @BeforeEach
    void setUp() {
        postServiceImpl = new PostServiceImpl();
    }

    @Test
    void testGetAllPosts() throws Exception {

        given(postService.getAllPosts()).willReturn(postServiceImpl.getAllPosts());

        mockMvc.perform(get(PostController.POST_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetByIdNotFound() throws Exception {

        given(postService.getPostById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(PostController.POST_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostById() throws Exception {

        Post testPost = postServiceImpl.getAllPosts().get(0);

        given(postService.getPostById(testPost.getPostId())).willReturn(Optional.of(testPost));

        mockMvc.perform(get(PostController.POST_PATH_ID, testPost.getPostId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postId", is(testPost.getPostId().toString())))
                .andExpect(jsonPath("$.title", is(testPost.getTitle())));
    }

    @Test
    void testCreatePost() throws Exception {

        Post testPost = postServiceImpl.getAllPosts().get(0);
        testPost.setPostId(null);
        testPost.setVersion(null);

        given(postService.createPost(any(Post.class))).willReturn(postServiceImpl.getAllPosts().get(1));

        mockMvc.perform(post(PostController.POST_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPost)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testEditPost() throws Exception {

        Post testPost = postServiceImpl.getAllPosts().get(0);

        mockMvc.perform(put(PostController.POST_PATH_ID, testPost.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPost)))
                .andExpect(status().isNoContent());

        verify(postService).editPost(any(UUID.class), any(Post.class));
    }

    @Test
    void testPatchPost() throws Exception {

        Post testPost = postServiceImpl.getAllPosts().get(0);

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("title", "newTitle");

        mockMvc.perform(patch(PostController.POST_PATH_ID, testPost.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postMap)))
                .andExpect(status().isNoContent());

        verify(postService).patchPost(uuidArgumentCaptor.capture(), postArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testPost.getPostId());
        assertThat(postArgumentCaptor.getValue().getTitle())
                .isEqualTo(postMap.get("title"));

    }

    @Test
    void testDeletePost() throws Exception {

        Post testPost = postServiceImpl.getAllPosts().get(0);

        mockMvc.perform(delete(PostController.POST_PATH_ID, testPost.getPostId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(postService).deletePost(uuidArgumentCaptor.capture());
        assertThat(testPost.getPostId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}