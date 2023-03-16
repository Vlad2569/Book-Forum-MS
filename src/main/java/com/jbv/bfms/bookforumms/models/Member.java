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
public class Member {

    private UUID memberId;
    private Integer version;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdate;
}