/*
 * Copyright (c) 2023. --jbv--
 */

package com.jbv.bfms.bookforumms.mappers;

import com.jbv.bfms.bookforumms.dtos.PostDto;
import com.jbv.bfms.bookforumms.models.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {

    Post postDtoToPost(PostDto postDto);

    PostDto postToPostDto(Post post);
}