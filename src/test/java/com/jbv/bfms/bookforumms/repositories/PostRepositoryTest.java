/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.repositories;

import com.jbv.bfms.bookforumms.models.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void testSavePost() {

        Post savedPost = postRepository.save(Post.builder()
                        .title("New Title")
                .build());

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getPostId()).isNotNull();
    }
}