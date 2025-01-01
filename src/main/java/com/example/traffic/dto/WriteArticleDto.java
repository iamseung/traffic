package com.example.traffic.dto;

import lombok.Getter;

@Getter
public class WriteArticleDto {
    Long boardId;
    String title;
    String content;
}
