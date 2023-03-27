package com.example.heroku.controller.order.form;

import com.example.heroku.service.order.request.ReceivedOrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderForm {
    private String token;
    private String impUid;
    private String merchantUid;
    private List<ReceivedOrderItem> items;
    private String deliveryMessage;


}
