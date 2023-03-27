package com.example.heroku.service.order;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.example.heroku.controller.order.form.OrderForm;
import com.example.heroku.controller.order.request.ChangeOrderStateRequest;
import com.example.heroku.controller.order.request.RefundRequest;
import com.example.heroku.entity.order.OrderInfo;
import com.example.heroku.entity.order.Payment;
import com.example.heroku.service.order.request.PaymentRegisterRequest;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    public Boolean registerOrderInfo(PaymentRegisterRequest paymentRegisterRequest);

    public Boolean refundAllOrder(RefundRequest refundRequest) throws IamportResponseException, IOException;

    public List<OrderInfo> readAllOrders(Long PaymentId);

    public OrderInfo readOrder(Long orderId);
    public List<Payment> readAllPayment(String token);

    public List<Payment> readRangePaymentList(String token , String rangeData);

    public List<Payment> readManagerAllPayment();

    public List<OrderInfo> changeOrderState(ChangeOrderStateRequest changeOrderStateRequest);

    public Integer salesAmount();

    public Boolean registerOrderInfo(OrderForm orderForm);
    Payment savePayment(OrderForm orderForm);

    public List<OrderInfo> reviewWritableList(String token);

}
