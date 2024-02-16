package com.gaming.api.models;

import com.gaming.domain.dto.PostDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostModel {
    private String content;
    private String author;
}
