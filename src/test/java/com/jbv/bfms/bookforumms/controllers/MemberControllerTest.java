/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.models.Member;
import com.jbv.bfms.bookforumms.services.MemberService;
import com.jbv.bfms.bookforumms.services.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    MemberServiceImpl memberServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    @Captor
    ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);

    @BeforeEach
    void setUp() {
        memberServiceImpl = new MemberServiceImpl();
    }

    @Test
    void testGetAllMembers() throws Exception {

        given(memberService.getAllMembers()).willReturn(memberServiceImpl.getAllMembers());

        mockMvc.perform(get(MemberController.MEMBER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetByIdNotFound() throws Exception {

        given(memberService.getMemberById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(MemberController.MEMBER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMemberById() throws Exception {

        Member testMember = memberServiceImpl.getAllMembers().get(0);

        given(memberService.getMemberById(testMember.getMemberId())).willReturn(Optional.of(testMember));

        mockMvc.perform(get(MemberController.MEMBER_PATH_ID, testMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId", is(testMember.getMemberId().toString())))
                .andExpect(jsonPath("$.username", is(testMember.getUsername())));
    }

    @Test
    void testCreateMember() throws Exception {

        Member testMember = memberServiceImpl.getAllMembers().get(0);
        testMember.setMemberId(null);
        testMember.setVersion(null);

        given(memberService.createMember(any(Member.class))).willReturn(memberServiceImpl.getAllMembers().get(1));

        mockMvc.perform(post(MemberController.MEMBER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateMember() throws Exception {

        Member testMember = memberServiceImpl.getAllMembers().get(0);

        mockMvc.perform(put(MemberController.MEMBER_PATH_ID, testMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isNoContent());

        verify(memberService).updateMember(any(UUID.class), any(Member.class));
    }

    @Test
    void testPatchMember() throws Exception {

        Member testMember = memberServiceImpl.getAllMembers().get(0);

        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("username", "newUsername");

        mockMvc.perform(patch(MemberController.MEMBER_PATH_ID, testMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberMap)))
                .andExpect(status().isNoContent());

        verify(memberService).patchMember(uuidArgumentCaptor.capture(), memberArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testMember.getMemberId());
        assertThat(memberArgumentCaptor.getValue().getUsername())
                .isEqualTo(memberMap.get("username"));

    }

    @Test
    void testDeleteMember() throws Exception {

        Member testMember = memberServiceImpl.getAllMembers().get(0);

        mockMvc.perform(delete(MemberController.MEMBER_PATH_ID, testMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(uuidArgumentCaptor.capture());
        assertThat(testMember.getMemberId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}