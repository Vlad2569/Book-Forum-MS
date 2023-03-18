/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.dtos.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService{

    private final Map<UUID, MemberDto> memberMap;

    public MemberServiceImpl() {

        memberMap = new HashMap<>();

        MemberDto memberDto1 = MemberDto.builder()
                .memberId(UUID.randomUUID())
                .version(1)
                .firstName("Peter")
                .lastName("Boychev")
                .email("pep_boychev@mail.me")
                .username("pepko")
                .password("951287346")
                .registeredAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        MemberDto memberDto2 = MemberDto.builder()
                .memberId(UUID.randomUUID())
                .version(1)
                .firstName("Michael")
                .lastName("Boychev")
                .email("mic_boychev@mail.me")
                .username("micky")
                .password("651287349")
                .registeredAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        MemberDto memberDto3 = MemberDto.builder()
                .memberId(UUID.randomUUID())
                .version(1)
                .firstName("Denis")
                .lastName("Boychev")
                .email("den_boychev@mail.me")
                .username("deni")
                .password("941287356")
                .registeredAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        memberMap.put(memberDto1.getMemberId(), memberDto1);
        memberMap.put(memberDto2.getMemberId(), memberDto2);
        memberMap.put(memberDto3.getMemberId(), memberDto3);
    }

    @Override
    public List<MemberDto> getAllMembers() {

        log.debug("Get All Members in service was called.");

        return new ArrayList<>(memberMap.values());
    }

    @Override
    public Optional<MemberDto> getMemberById(UUID memberId) {

        log.debug("Get Post By Id in service was called. Id: " + memberId.toString());

        return Optional.of(memberMap.get(memberId));
    }

    @Override
    public MemberDto createMember(MemberDto memberDto) {

        MemberDto newMemberDto = MemberDto.builder()
                .memberId(UUID.randomUUID())
                .version(1)
                .firstName(memberDto.getFirstName())
                .lastName(memberDto.getLastName())
                .email(memberDto.getEmail())
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .registeredAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        memberMap.put(newMemberDto.getMemberId(), newMemberDto);

        log.debug("Create Member in service was called. Id: " + newMemberDto.getMemberId().toString());

        return newMemberDto;
    }

    @Override
    public Optional<MemberDto> updateMember(UUID memberId, MemberDto memberDto) {

        MemberDto memberDtoToUpdate = memberMap.get(memberId);

        memberDtoToUpdate.setFirstName(memberDto.getFirstName());
        memberDtoToUpdate.setLastName(memberDto.getLastName());
        memberDtoToUpdate.setEmail(memberDtoToUpdate.getEmail());
        memberDtoToUpdate.setPassword(memberDto.getPassword());
        memberDtoToUpdate.setLastUpdate(LocalDateTime.now());

        log.debug("Update Member in service was called. Id: " + memberDtoToUpdate.getMemberId().toString());
        return Optional.of(memberDtoToUpdate);
    }

    @Override
    public Optional<MemberDto> patchMember(UUID memberId, MemberDto memberDto) {

        MemberDto memberDtoToPatch = memberMap.get(memberId);

        if (StringUtils.hasText(memberDto.getFirstName())) {
            memberDtoToPatch.setFirstName(memberDto.getFirstName());
        }

        if (StringUtils.hasText(memberDto.getLastName())) {
            memberDtoToPatch.setLastName(memberDto.getLastName());
        }

        if (StringUtils.hasText(memberDto.getEmail())) {
            memberDtoToPatch.setEmail(memberDto.getEmail());
        }

        if (StringUtils.hasText(memberDto.getPassword())) {
            memberDtoToPatch.setPassword(memberDto.getPassword());
        }

        memberDtoToPatch.setLastUpdate(LocalDateTime.now());

        log.debug("Patch Member in service was called. Id: " + memberDtoToPatch.getMemberId().toString());
        return Optional.of(memberDtoToPatch);
    }

    @Override
    public Boolean deleteMember(UUID memberId) {

        memberMap.remove(memberId);

        log.debug("Delete Member in service was called. Id: " + memberId.toString());

        return true;
    }
}