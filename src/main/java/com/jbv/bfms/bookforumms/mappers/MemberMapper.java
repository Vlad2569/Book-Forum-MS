/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.mappers;

import com.jbv.bfms.bookforumms.dtos.MemberDto;
import com.jbv.bfms.bookforumms.models.Member;
import org.mapstruct.Mapper;

@Mapper
public interface MemberMapper {

    Member memberDtoToMember(MemberDto memberDto);
    MemberDto memberToMemberDto(Member member);
}