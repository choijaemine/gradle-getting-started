package com.example.heroku.controller.tour.form;

import com.example.heroku.service.tour.request.PaymentReservationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentReservationForm {

    private String merchant_uid;
    private Long reservationId;
    private Long memberId;
    private Integer totalPaymentPrice;
    private boolean paymentState;

    public PaymentReservationRequest toPaymentReservationRequest(){
        return new PaymentReservationRequest(merchant_uid,reservationId,memberId,totalPaymentPrice,paymentState);
    }
}
