package com.gaming.api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyModel {
    private String content;
    private String author;
    private String inReplyAt;
}
