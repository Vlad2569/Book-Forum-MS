/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class PostDto {

    private UUID postId;
    private Integer version;
    @NotEmpty
    @NotNull
    private String title;

    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}