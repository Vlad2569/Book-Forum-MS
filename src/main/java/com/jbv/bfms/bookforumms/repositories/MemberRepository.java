/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.repositories;

import com.jbv.bfms.bookforumms.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

}