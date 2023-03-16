/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.models;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Post {

    private UUID postId;
    private Integer version;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}