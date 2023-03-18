/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.controllers;

import com.jbv.bfms.bookforumms.exceptions.NotFoundException;
import com.jbv.bfms.bookforumms.dtos.MemberDto;
import com.jbv.bfms.bookforumms.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    public static final String MEMBER_PATH = "/api/v1/member";
    public static final String MEMBER_PATH_ID = MEMBER_PATH + "/{memberId}";

    private final MemberService memberService;

    @GetMapping(MEMBER_PATH)
    public List<MemberDto> getAllMembers() {

        log.debug("Get All Members in controller was called.");

        return memberService.getAllMembers();
    }

    @GetMapping(MEMBER_PATH_ID)
    public MemberDto getMemberById(@PathVariable("memberId") UUID memberId) {

        log.debug("Get Member By Id in controller was called.");

        return memberService.getMemberById(memberId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(MEMBER_PATH)
    public ResponseEntity createMember(@RequestBody MemberDto memberDto) {

        log.debug("Create Member in controller was called.");

        MemberDto newMemberDto = memberService.createMember(memberDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", MEMBER_PATH + "/" + newMemberDto.getMemberId().toString());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(MEMBER_PATH_ID)
    public ResponseEntity updateMember(@PathVariable("memberId") UUID memberId, @RequestBody MemberDto memberDto) {

        log.debug("Update Member in controller was called.");

        if (memberService.updateMember(memberId, memberDto).isEmpty()) {
            throw new NotFoundException();
        };

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(MEMBER_PATH_ID)
    public ResponseEntity patchMember(@PathVariable("memberId") UUID memberId, @RequestBody MemberDto memberDto) {

        log.debug("Patch Member in controller was called.");

        if (memberService.patchMember(memberId, memberDto).isEmpty()) {
            throw new NotFoundException();
        };

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(MEMBER_PATH_ID)
    public ResponseEntity deleteMember(@PathVariable("memberId") UUID memberId) {

        log.debug("Delete Member in controller was called.");

        if (!memberService.deleteMember(memberId)) {
            throw  new NotFoundException();
        };

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}