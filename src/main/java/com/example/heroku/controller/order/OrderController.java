package com.example.heroku.controller.order;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.example.heroku.controller.member.form.MemberLoggedInTokenForm;
import com.example.heroku.controller.order.form.OrderForm;
import com.example.heroku.controller.order.form.PaymentDataRangeForm;
import com.example.heroku.controller.order.form.PaymentRegisterForm;
import com.example.heroku.controller.order.request.ChangeOrderStateRequest;
import com.example.heroku.controller.order.request.RefundRequest;
import com.example.heroku.entity.order.OrderInfo;
import com.example.heroku.entity.order.Payment;
import com.example.heroku.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("ztz/order")
@CrossOrigin(origins = {"http://localhost:8080" , "https://tlst-ztz.web.app/"}, allowedHeaders = "*")
public class OrderController {


    @Autowired
    private OrderService service;

    @PostMapping("/OrderRegister")
    public Boolean orderRegister(@RequestBody PaymentRegisterForm paymentRegisterForm){
        log.info("orderRegister" + paymentRegisterForm);
        return service.registerOrderInfo(paymentRegisterForm.toOrderRegisterRequest());
    }

    // 주문관리 - 상세보기
    @PostMapping("/ReadAllOrder/{paymentId}")
    public List<OrderInfo> ReadAllOrder(@PathVariable("paymentId") Long paymentId){
        log.info("ReadAllOrder" + paymentId);

        List<OrderInfo> orderInfoList = service.readAllOrders(paymentId);
        System.out.println(orderInfoList.get(0).getOrderID());
        return orderInfoList;
    }
    //주문관리 리스트
    @PostMapping("/ReadAllPayment")
    public List<Payment> ReadAllPayment(@RequestBody MemberLoggedInTokenForm memberLoggedInTokenForm){
        if (memberLoggedInTokenForm.getToken() == null|| memberLoggedInTokenForm.getToken().length() == 0){

            List<Payment> payments =  service.readManagerAllPayment();
            return payments;

        }else {
            String SubString = "";
            if(memberLoggedInTokenForm.getToken().length() >= 37){
                SubString = memberLoggedInTokenForm.getToken().substring(1,37);
            }else {
                SubString = memberLoggedInTokenForm.getToken();
            }
            List<Payment> payments = service.readAllPayment(SubString);
            return payments;
        }
    }

    @PostMapping("/readPayment/byDate")
    public List<Payment> readPaymentByData(@RequestBody PaymentDataRangeForm paymentDataRangeForm){
        log.info(paymentDataRangeForm.getReadData());
        String readData = paymentDataRangeForm.getReadData();
        String SubString = "";
        if(paymentDataRangeForm.getToken().length() >= 37){
            SubString = paymentDataRangeForm.getToken().substring(1,37);
        }else {
            SubString = paymentDataRangeForm.getToken();
        }
        List<Payment> payments = service.readRangePaymentList(SubString, readData);

        return payments;
    }

    @PostMapping("/refundAllOrder/{refundPaymentId}")
    public Boolean refundAllOrder(@PathVariable("refundPaymentId") Long refundPaymentId, @RequestBody RefundRequest refundRequest) throws IamportResponseException, IOException {
        log.info("refundAllOrder" + refundPaymentId);
        log.info("refundAllOrder" + refundRequest.getRefundReason());
        log.info("refundAllOrder" + refundRequest.getRefundPaymentId());

        return service.refundAllOrder(refundRequest);
    }



    @PostMapping("/changeOrderState")
    public List<OrderInfo> changeOrderState( @RequestBody ChangeOrderStateRequest changeOrderStateRequest){
        log.info("주문상태 변경 주문아이디: " + changeOrderStateRequest.getOrderId());
        log.info("주문상태 변경 타입: " + changeOrderStateRequest.getReqType());
        log.info("주문상태 변경 결제번호: " + changeOrderStateRequest.getPaymentId());
        List<OrderInfo> orderInfoList = service.changeOrderState(changeOrderStateRequest);
        return orderInfoList;
    }

    @GetMapping(path = "/salesAmount")
    public Integer salesAmount() {
        return service.salesAmount();
    }


    @PostMapping("/save-order")
    public Boolean order(@RequestBody OrderForm orderForm){
        log.info("orderRegister" + orderForm);
        return service.registerOrderInfo(orderForm);
    }
    @PostMapping("/writableReview")
    public List<OrderInfo> writableReview(@RequestBody MemberLoggedInTokenForm form){
        log.info("writableReview");
        String SubString = "";
        if(form.getToken().length() >= 37){
            SubString = form.getToken().substring(1,37);
        }else {
            SubString = form.getToken();
        }
        log.info("writableReview" + SubString );

        List<OrderInfo> orderInfoList = service.reviewWritableList(SubString);
        return orderInfoList;
    }

    @PostMapping("/readOrder/{orderInfoId}")
    public OrderInfo readOrderInfo(@PathVariable("orderInfoId") Long orderInfoId){
        log.info("readOrderInfo" + orderInfoId.toString() );

        return service.readOrder(orderInfoId);
    }
}
