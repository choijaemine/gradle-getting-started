package com.example.heroku.controller.boards.request;

import lombok.Getter;

@Getter
public class BoardsRequest {
    private String title;
    private String content;
    private Long memberId;
    private String categoryType;
    private Boolean privateCheck;
}
