package com.example.heroku.controller.order.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    private Long refundPaymentId;
    private String refundReason;

    public RefundRequest toRefundRequest() {
        return new RefundRequest(refundPaymentId, refundReason);
    }
}
