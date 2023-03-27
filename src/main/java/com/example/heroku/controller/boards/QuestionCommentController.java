package com.example.heroku.controller.boards;

import com.example.heroku.controller.boards.request.CommentRequest;
import com.example.heroku.entity.boards.QuestionComment;
import com.example.heroku.service.boards.QuestionCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("ztz/boards/question/comment")
@CrossOrigin(origins = {"http://localhost:8080" , "https://tlst-ztz.web.app/"}, allowedHeaders = "*")
public class QuestionCommentController {

    @Autowired
    QuestionCommentService questionCommentService;

    @GetMapping("/{questionNo}")
    public List<QuestionComment> questionCommentList(@PathVariable("questionNo") Long questionNo) {
        log.info("questionCommentList()");

        return questionCommentService.questionCommentList(questionNo);
    }

    @GetMapping("read/{questionNo}")
    public QuestionComment questionComment(@PathVariable("questionNo") Long questionNo) {
        log.info("questionCommentList()");

        return questionCommentService.questionComment(questionNo);
    }

    @PostMapping("/register")
    public void questionCommentRegister(@RequestBody CommentRequest commentRequest) {
        log.info("questionCommentRegister()" + commentRequest.getQuestion_no());
        log.info(commentRequest.getComment());
        questionCommentService.questionCommentRegister(commentRequest);
    }

    @PutMapping("/modify/{questionCommentNo}")
    public void modifyQuestionComment(@PathVariable("questionCommentNo") Long questionCommentNo,
                                      @RequestBody CommentRequest commentRequest) {
        log.info("modifyComment");

        questionCommentService.modifyQuestionComment(questionCommentNo, commentRequest);
    }

    @DeleteMapping("/{questionCommentNo}")
    public void questionCommentRemove (@PathVariable("questionCommentNo") Long questionCommentNo) {
        log.info("questionBoardRemove()");

        questionCommentService.questionCommentRemove(questionCommentNo);
    }
}