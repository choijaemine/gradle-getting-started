package com.example.heroku.controller.order.request;

import lombok.Data;

@Data
public class ChangeItemCountRequest {

    private Long itemNo;
    private int count;
    private int selectedProductAmount;
}
