/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.MemberDto;
import com.jbv.bfms.bookforumms.mappers.MemberMapper;
import com.jbv.bfms.bookforumms.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
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

        AtomicReference<Optional<MemberDto>> atomicReference = new AtomicReference<>();

        memberRepository.findById(memberId).ifPresentOrElse(foundMember -> {
            foundMember.setFirstName(memberDto.getFirstName());
            foundMember.setLastName(memberDto.getLastName());
            foundMember.setEmail(memberDto.getEmail());
            foundMember.setPassword(memberDto.getPassword());
            atomicReference.set(Optional.of(memberMapper.memberToMemberDto(memberRepository.save(foundMember))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        log.debug("Update Member in service was called. Id: " + memberId);

        return atomicReference.get();
    }

    @Override
    public Optional<MemberDto> patchMember(UUID memberId, MemberDto memberDto) {

        AtomicReference<Optional<MemberDto>> atomicReference = new AtomicReference<>();

        memberRepository.findById(memberId).ifPresentOrElse(foundMember -> {
            if (StringUtils.hasText(memberDto.getFirstName())) {
                foundMember.setFirstName(memberDto.getFirstName());
            }
            if (StringUtils.hasText(memberDto.getLastName())) {
                foundMember.setLastName(memberDto.getLastName());
            }
            if (StringUtils.hasText(memberDto.getEmail())) {
                foundMember.setEmail(memberDto.getEmail());
            }
            if (StringUtils.hasText(memberDto.getPassword())) {
                foundMember.setPassword(memberDto.getPassword());
            }
            atomicReference.set(Optional.of(memberMapper.memberToMemberDto(memberRepository.save(foundMember))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        log.debug("Patch Member in service was called. Id: " + memberId);

        return atomicReference.get();
    }

    @Override
    public Boolean deleteMember(UUID memberId) {

        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }
}