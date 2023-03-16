/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.models.Member;

import java.util.List;
import java.util.UUID;

public interface MemberService {
    List<Member> getAllMembers();

    Member getMemberById(UUID memberId);

    Member createMember(Member member);

    void updateMember(UUID memberId, Member member);

    void patchMember(UUID memberId, Member member);

    void deleteMember(UUID memberId);
}