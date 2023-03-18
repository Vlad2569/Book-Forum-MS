/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.repositories;

import com.jbv.bfms.bookforumms.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

}