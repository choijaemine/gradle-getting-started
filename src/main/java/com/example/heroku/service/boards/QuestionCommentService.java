package com.example.heroku.service.boards;

import com.example.heroku.controller.boards.request.CommentRequest;
import com.example.heroku.entity.boards.QuestionComment;

import java.util.List;

public interface QuestionCommentService {
    public List<QuestionComment> questionCommentList(Long questionNo);

    public QuestionComment questionComment(Long questionNo);

    public void questionCommentRegister(CommentRequest commentRequest);

    public void modifyQuestionComment(Long questionCommentNo, CommentRequest commentRequest);

    public void questionCommentRemove(Long questionCommentNo);

}