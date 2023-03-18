/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.MemberDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {
    List<MemberDto> getAllMembers();

    Optional<MemberDto> getMemberById(UUID memberId);

    MemberDto createMember(MemberDto memberDto);

    Optional<MemberDto> updateMember(UUID memberId, MemberDto memberDto);

    Optional<MemberDto> patchMember(UUID memberId, MemberDto memberDto);

    Boolean deleteMember(UUID memberId);
}