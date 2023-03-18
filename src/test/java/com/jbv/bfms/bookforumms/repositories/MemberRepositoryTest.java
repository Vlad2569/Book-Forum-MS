/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.repositories;

import com.jbv.bfms.bookforumms.models.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testSaveMember() {

        Member savedMember = memberRepository.save(Member.builder()
                        .username("New Username")
                .build());

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isNotNull();
    }
}