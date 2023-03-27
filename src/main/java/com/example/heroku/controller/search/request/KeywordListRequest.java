package com.example.heroku.controller.search.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class KeywordListRequest {

    private List<Long> selectedKeywords;
}
