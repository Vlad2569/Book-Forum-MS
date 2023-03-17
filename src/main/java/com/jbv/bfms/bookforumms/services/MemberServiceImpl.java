/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.services;

import com.jbv.bfms.bookforumms.models.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService{

    private final Map<UUID, Member> memberMap;

    public MemberServiceImpl() {

        memberMap = new HashMap<>();

        Member member1 = Member.builder()
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

        Member member2 = Member.builder()
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

        Member member3 = Member.builder()
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

        memberMap.put(member1.getMemberId(), member1);
        memberMap.put(member2.getMemberId(), member2);
        memberMap.put(member3.getMemberId(), member3);
    }

    @Override
    public List<Member> getAllMembers() {

        log.debug("Get All Members in service was called.");

        return new ArrayList<>(memberMap.values());
    }

    @Override
    public Optional<Member> getMemberById(UUID memberId) {

        log.debug("Get Post By Id in service was called. Id: " + memberId.toString());

        return Optional.of(memberMap.get(memberId));
    }

    @Override
    public Member createMember(Member member) {

        Member newMember = Member.builder()
                .memberId(UUID.randomUUID())
                .version(1)
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .email(member.getEmail())
                .username(member.getUsername())
                .password(member.getPassword())
                .registeredAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        memberMap.put(newMember.getMemberId(), newMember);

        log.debug("Create Member in service was called. Id: " + newMember.getMemberId().toString());

        return newMember;
    }

    @Override
    public void updateMember(UUID memberId, Member member) {

        Member memberToUpdate = memberMap.get(memberId);

        memberToUpdate.setFirstName(member.getFirstName());
        memberToUpdate.setLastName(member.getLastName());
        memberToUpdate.setEmail(memberToUpdate.getEmail());
        memberToUpdate.setPassword(member.getPassword());
        memberToUpdate.setLastUpdate(LocalDateTime.now());

        log.debug("Update Member in service was called. Id: " + memberToUpdate.getMemberId().toString());
    }

    @Override
    public void patchMember(UUID memberId, Member member) {

        Member memberToPatch = memberMap.get(memberId);

        if (StringUtils.hasText(member.getFirstName())) {
            memberToPatch.setFirstName(member.getFirstName());
        }

        if (StringUtils.hasText(member.getLastName())) {
            memberToPatch.setLastName(member.getLastName());
        }

        if (StringUtils.hasText(member.getEmail())) {
            memberToPatch.setEmail(member.getEmail());
        }

        if (StringUtils.hasText(member.getPassword())) {
            memberToPatch.setPassword(member.getPassword());
        }

        memberToPatch.setLastUpdate(LocalDateTime.now());

        log.debug("Patch Member in service was called. Id: " + memberToPatch.getMemberId().toString());
    }

    @Override
    public void deleteMember(UUID memberId) {

        memberMap.remove(memberId);

        log.debug("Delete Member in service was called. Id: " + memberId.toString());
    }
}