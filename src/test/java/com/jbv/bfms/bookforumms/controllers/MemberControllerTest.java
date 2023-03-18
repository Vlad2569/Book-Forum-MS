/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbv.bfms.bookforumms.dtos.MemberDto;
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
    ArgumentCaptor<MemberDto> memberArgumentCaptor = ArgumentCaptor.forClass(MemberDto.class);

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

        MemberDto testMemberDto = memberServiceImpl.getAllMembers().get(0);

        given(memberService.getMemberById(testMemberDto.getMemberId())).willReturn(Optional.of(testMemberDto));

        mockMvc.perform(get(MemberController.MEMBER_PATH_ID, testMemberDto.getMemberId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId", is(testMemberDto.getMemberId().toString())))
                .andExpect(jsonPath("$.username", is(testMemberDto.getUsername())));
    }

    @Test
    void testCreateMember() throws Exception {

        MemberDto testMemberDto = memberServiceImpl.getAllMembers().get(0);
        testMemberDto.setMemberId(null);
        testMemberDto.setVersion(null);

        given(memberService.createMember(any(MemberDto.class))).willReturn(memberServiceImpl.getAllMembers().get(1));

        mockMvc.perform(post(MemberController.MEMBER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMemberDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateMember() throws Exception {

        MemberDto testMemberDto = memberServiceImpl.getAllMembers().get(0);

        given(memberService.updateMember(any(), any())).willReturn(Optional.of(testMemberDto));

        mockMvc.perform(put(MemberController.MEMBER_PATH_ID, testMemberDto.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMemberDto)))
                .andExpect(status().isNoContent());

        verify(memberService).updateMember(any(UUID.class), any(MemberDto.class));
    }

    @Test
    void testPatchMember() throws Exception {

        MemberDto testMemberDto = memberServiceImpl.getAllMembers().get(0);

        given(memberService.patchMember(any(), any())).willReturn(Optional.of(testMemberDto));

        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("username", "newUsername");

        mockMvc.perform(patch(MemberController.MEMBER_PATH_ID, testMemberDto.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberMap)))
                .andExpect(status().isNoContent());

        verify(memberService).patchMember(uuidArgumentCaptor.capture(), memberArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testMemberDto.getMemberId());
        assertThat(memberArgumentCaptor.getValue().getUsername())
                .isEqualTo(memberMap.get("username"));

    }

    @Test
    void testDeleteMember() throws Exception {

        MemberDto testMemberDto = memberServiceImpl.getAllMembers().get(0);

        given(memberService.deleteMember(any())).willReturn(true);

        mockMvc.perform(delete(MemberController.MEMBER_PATH_ID, testMemberDto.getMemberId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(uuidArgumentCaptor.capture());
        assertThat(testMemberDto.getMemberId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}