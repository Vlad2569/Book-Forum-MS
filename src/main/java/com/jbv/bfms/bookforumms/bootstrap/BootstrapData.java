/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.bootstrap;

import com.jbv.bfms.bookforumms.models.Member;
import com.jbv.bfms.bookforumms.models.Post;
import com.jbv.bfms.bookforumms.repositories.MemberRepository;
import com.jbv.bfms.bookforumms.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {

        loadMemberData();
        loadPostData();
    }

    private void loadMemberData() {
        if (memberRepository.count() == 0) {
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

            memberRepository.saveAll(Arrays.asList(member1, member2, member3));
        }
    }

    private void loadPostData() {
        if (postRepository.count() == 0) {
            Post post1 = Post.builder()
                    .postId(UUID.randomUUID())
                    .version(1)
                    .title("First Forum Post")
                    .body("Explain the idea and specifics of the REST API" +
                            " + Client UI architecture for web applications.")
                    .createdAt(LocalDateTime.now())
                    .lastUpdate(LocalDateTime.now())
                    .build();

            Post post2 = Post.builder()
                    .postId(UUID.randomUUID())
                    .version(1)
                    .title("Second Forum Post")
                    .body("Explain the MVC design pattern and " +
                            "how it can be combined with a layered achitecture.")
                    .createdAt(LocalDateTime.now())
                    .lastUpdate(LocalDateTime.now())
                    .build();

            Post post3 = Post.builder()
                    .postId(UUID.randomUUID())
                    .version(1)
                    .title("Third Forum Post")
                    .body("Understand the MVC design pattern and " +
                            "how it fits in the layered architecture.")
                    .createdAt(LocalDateTime.now())
                    .lastUpdate(LocalDateTime.now())
                    .build();

            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
        }
    }
}