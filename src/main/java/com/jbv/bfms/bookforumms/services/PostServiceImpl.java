/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    private final Map<UUID, PostDto> postMap;

    public PostServiceImpl() {
        this.postMap = new HashMap<>();

        PostDto postDto1 = PostDto.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("First Forum Post")
                .body("Explain the idea and specifics of the REST API" +
                        " + Client UI architecture for web applications.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        PostDto postDto2 = PostDto.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("Second Forum Post")
                .body("Explain the MVC design pattern and " +
                        "how it can be combined with a layered achitecture.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        PostDto postDto3 = PostDto.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title("Third Forum Post")
                .body("Understand the MVC design pattern and " +
                        "how it fits in the layered architecture.")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        postMap.put(postDto1.getPostId(), postDto1);
        postMap.put(postDto2.getPostId(), postDto2);
        postMap.put(postDto3.getPostId(), postDto3);
    }

    @Override
    public List<PostDto> getAllPosts() {

        log.debug("Get All Posts in service was called.");

        return new ArrayList<>(postMap.values());
    }

    @Override
    public Optional<PostDto> getPostById(UUID postId) {

        log.debug("Get Post By Id in service was called. Id: " + postId.toString());

        return Optional.of(postMap.get(postId));
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        PostDto newPostDto = PostDto.builder()
                .postId(UUID.randomUUID())
                .version(1)
                .title(postDto.getTitle())
                .body(postDto.getBody())
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        postMap.put(newPostDto.getPostId(), newPostDto);

        log.debug("Create Post in service was called. Id: " + newPostDto.getPostId().toString());

        return newPostDto;
    }

    @Override
    public Optional<PostDto> editPost(UUID postId, PostDto postDto) {

        PostDto postDtoToEdit = postMap.get(postId);

        postDtoToEdit.setTitle(postDto.getTitle());
        postDtoToEdit.setBody(postDto.getBody());
        postDtoToEdit.setLastUpdate(LocalDateTime.now());

        log.debug("Edit Post in service was called. Id: " + postDtoToEdit.getPostId().toString());
        return Optional.of(postDtoToEdit);
    }

    @Override
    public Optional<PostDto> patchPost(UUID postId, PostDto postDto) {

        PostDto postDtoToPatch = postMap.get(postId);

        if (StringUtils.hasText(postDto.getTitle())) {
            postDtoToPatch.setTitle(postDto.getTitle());
        }

        if (StringUtils.hasText(postDto.getBody())) {
            postDtoToPatch.setBody(postDto.getBody());
        }

        postDtoToPatch.setLastUpdate(LocalDateTime.now());

        log.debug("Patch Post in service was called. Id: " + postDtoToPatch.getPostId().toString());
        return Optional.of(postDtoToPatch);
    }

    @Override
    public Boolean deletePost(UUID postId) {

        postMap.remove(postId);

        log.debug("Delete Post in service was called. Id: " + postId.toString());
        return true;
    }
}