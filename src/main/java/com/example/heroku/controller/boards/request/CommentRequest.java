package com.example.heroku.controller.boards.request;

import lombok.Getter;

@Getter
public class CommentRequest {

    private String comment;
    private Long question_no;
    private Long member_no;
}