package com.example.heroku.service.order.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceivedOrderItem {

    private Long itemNo;
    private Long productNo;
    private String productName;
    private int count;
    private int selectedProductAmount;


}
