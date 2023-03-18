/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID memberId;
    @Version
    private Integer version;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdate;
}