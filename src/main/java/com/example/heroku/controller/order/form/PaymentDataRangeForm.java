package com.example.heroku.controller.order.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentDataRangeForm {
    String token;
    String readData;
}
