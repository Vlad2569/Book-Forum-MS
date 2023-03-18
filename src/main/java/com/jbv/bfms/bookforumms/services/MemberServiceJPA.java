/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.MemberDto;
import com.jbv.bfms.bookforumms.mappers.MemberMapper;
import com.jbv.bfms.bookforumms.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class MemberServiceJPA implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::memberToMemberDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MemberDto> getMemberById(UUID memberId) {
        return Optional.ofNullable(memberMapper.memberToMemberDto(memberRepository.findById(memberId)
                .orElse(null)));
    }

    @Override
    public MemberDto createMember(MemberDto memberDto) {
        return memberMapper.memberToMemberDto(memberRepository.save(memberMapper.memberDtoToMember(memberDto)));
    }

    @Override
    public Optional<MemberDto> updateMember(UUID memberId, MemberDto memberDto) {

        return null;
    }

    @Override
    public Optional<MemberDto> patchMember(UUID memberId, MemberDto memberDto) {

        return null;
    }

    @Override
    public Boolean deleteMember(UUID memberId) {

        return null;
    }
}