package com.example.heroku.service.boards;

import com.example.heroku.controller.boards.request.BoardsRequest;
import com.example.heroku.entity.boards.QuestionBoard;

import java.util.List;

public interface QuestionService {
    // 질문게시판 리스트 UI
    public List<QuestionBoard> questionList();
    public List<QuestionBoard> memberQuestionList(String token);
     // 질문게시판 조회(읽기)
    public QuestionBoard questionRead(Long questionNo);
     // 질문게시판 게시물 등록
    public void questionRegister(BoardsRequest boardsRequest);
     //질문게시판 게시물 수정
    public void modify(Long questionNo, BoardsRequest boardsRequest);
    // 질문게시판 게시물 삭제
    public void questionRemove(Long questionNo);
}
