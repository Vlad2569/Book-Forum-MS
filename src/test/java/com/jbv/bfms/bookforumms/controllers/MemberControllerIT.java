/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.jbv.bfms.bookforumms.dtos.MemberDto;
import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.mappers.MemberMapper;
import com.jbv.bfms.bookforumms.models.Member;
import com.jbv.bfms.bookforumms.repositories.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberControllerIT {

    @Autowired
    MemberController memberController;
    
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberMapper memberMapper;

    @Test
    void testGetAllMembers() {
        List<MemberDto> memberDtos = memberController.getAllMembers();

        assertThat(memberDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {

        memberRepository.deleteAll();

        List<MemberDto> memberDtos = memberController.getAllMembers();

        assertThat(memberDtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById() {

        Member testMember = memberRepository.findAll().get(0);

        MemberDto memberDto = memberController.getMemberById(testMember.getMemberId());

        assertThat(memberDto).isNotNull();
    }

    @Test
    void testNotFound() {

        assertThrows(NotFoundException.class, () -> {
            memberController.getMemberById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testCreateMember() {

        MemberDto memberDto = MemberDto.builder()
                .username("New Username")
                .build();

        ResponseEntity responseEntity = memberController.createMember(memberDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Member member = memberRepository.findById(savedUUID).get();
        assertThat(member).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateMember() {

        Member testMember = memberRepository.findAll().get(0);
        MemberDto memberDto = memberMapper.memberToMemberDto(testMember);
        memberDto.setMemberId(null);
        memberDto.setVersion(null);
        final String memberName = "UPDATED";
        memberDto.setFirstName(memberName);

        ResponseEntity responseEntity = memberController.updateMember(testMember.getMemberId(), memberDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Member updatedMember = memberRepository.findById(testMember.getMemberId()).get();
        assertThat(updatedMember.getFirstName()).isEqualTo(memberName);
    }

    @Test
    void testUpdateNotFound() {

        assertThrows(NotFoundException.class, () -> {
            memberController.updateMember(UUID.randomUUID(), MemberDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteMember() {

        Member testMember = memberRepository.findAll().get(0);

        ResponseEntity responseEntity = memberController.deleteMember(testMember.getMemberId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(memberRepository.findById(testMember.getMemberId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() {

        assertThrows(NotFoundException.class, () -> {
            memberController.deleteMember(UUID.randomUUID());
        });
    }
}