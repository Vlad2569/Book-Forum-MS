/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.dtos;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class MemberDto {

    private UUID memberId;
    private Integer version;

    private String firstName;

    private String lastName;

    private String email;
    @NotEmpty
    @NotNull
    private String username;

    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdate;
}