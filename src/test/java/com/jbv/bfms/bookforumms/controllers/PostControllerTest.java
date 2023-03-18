/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbv.bfms.bookforumms.dtos.PostDto;
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
import org.springframework.test.web.servlet.MvcResult;

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
    ArgumentCaptor<PostDto> postArgumentCaptor = ArgumentCaptor.forClass(PostDto.class);

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

        PostDto testPostDto = postServiceImpl.getAllPosts().get(0);

        given(postService.getPostById(testPostDto.getPostId())).willReturn(Optional.of(testPostDto));

        mockMvc.perform(get(PostController.POST_PATH_ID, testPostDto.getPostId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postId", is(testPostDto.getPostId().toString())))
                .andExpect(jsonPath("$.title", is(testPostDto.getTitle())));
    }

    @Test
    void testCreatePost() throws Exception {

        PostDto testPostDto = postServiceImpl.getAllPosts().get(0);
        testPostDto.setPostId(null);
        testPostDto.setVersion(null);

        given(postService.createPost(any(PostDto.class))).willReturn(postServiceImpl.getAllPosts().get(1));

        mockMvc.perform(post(PostController.POST_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPostDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreatePostNullTitle() throws Exception {

        PostDto testPostDto = PostDto.builder().build();

        given(postService.createPost(any(PostDto.class))).willReturn(postServiceImpl.getAllPosts().get(1));

        MvcResult mvcResult = mockMvc.perform(post(PostController.POST_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPostDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testEditPost() throws Exception {

        PostDto testPostDto = postServiceImpl.getAllPosts().get(0);

        given(postService.editPost(any(), any())).willReturn(Optional.of(testPostDto));

        mockMvc.perform(put(PostController.POST_PATH_ID, testPostDto.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPostDto)))
                .andExpect(status().isNoContent());

        verify(postService).editPost(any(UUID.class), any(PostDto.class));
    }

    @Test
    void testPatchPost() throws Exception {

        PostDto testPostDto = postServiceImpl.getAllPosts().get(0);

        given(postService.patchPost(any(), any())).willReturn(Optional.of(testPostDto));

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("title", "newTitle");

        mockMvc.perform(patch(PostController.POST_PATH_ID, testPostDto.getPostId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postMap)))
                .andExpect(status().isNoContent());

        verify(postService).patchPost(uuidArgumentCaptor.capture(), postArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testPostDto.getPostId());
        assertThat(postArgumentCaptor.getValue().getTitle())
                .isEqualTo(postMap.get("title"));

    }

    @Test
    void testDeletePost() throws Exception {

        PostDto testPostDto = postServiceImpl.getAllPosts().get(0);

        given(postService.deletePost(any())).willReturn(true);

        mockMvc.perform(delete(PostController.POST_PATH_ID, testPostDto.getPostId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(postService).deletePost(uuidArgumentCaptor.capture());
        assertThat(testPostDto.getPostId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}