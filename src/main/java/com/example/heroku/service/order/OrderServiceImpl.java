package com.example.heroku.service.order;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.example.heroku.controller.order.form.OrderForm;
import com.example.heroku.controller.order.form.OrderInfoRegisterForm;
import com.example.heroku.controller.order.request.ChangeOrderStateRequest;
import com.example.heroku.controller.order.request.RefundRequest;
import com.example.heroku.entity.member.Address;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.member.MemberProfile;
import com.example.heroku.entity.order.OrderInfo;
import com.example.heroku.entity.order.Payment;
import com.example.heroku.entity.order.PaymentState;
import com.example.heroku.entity.products.Product;
import com.example.heroku.repository.member.MemberRepository;
import com.example.heroku.repository.order.OrderInfoRepository;
import com.example.heroku.repository.order.PaymentRepository;
import com.example.heroku.repository.products.ProductsRepository;
import com.example.heroku.service.order.request.PaymentRegisterRequest;
import com.example.heroku.service.order.request.ReceivedOrderItem;
import com.example.heroku.service.security.RedisService;
import com.example.heroku.utility.order.setRandomOrderNo;
import com.example.heroku.utility.order.validationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final Integer MINORDERNUM = 1;
    private final Integer MAXORDERNUM = 99999;

    @Autowired
    OrderInfoRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    RedisService redisService;
    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public Boolean registerOrderInfo(PaymentRegisterRequest paymentRegisterRequest) {
        try {
            //결제 정보 저장
            Payment payment = registerPayment(paymentRegisterRequest);

            OrderInfoRegisterForm OrderListInfo = paymentRegisterRequest.getSendInfo();

            // 주문 번호 생성
            String setOrderNum = MakeOrderedNo(OrderListInfo.getMemberID().get(0));

            for (int i = 0; i < OrderListInfo.getProductID().size(); i++) {
                Optional<Product> maybeProduct = productsRepository.findById(OrderListInfo.getProductID().get(i));
                Optional<Member> maybeMember = memberRepository.findById(OrderListInfo.getMemberID().get(i));

                OrderInfo orderInfo = OrderInfo
                        .builder()
                        .orderNo(setOrderNum)
                        .orderCnt(OrderListInfo.getOrderCnt().get(i))
                        .orderState(PaymentState.PAYMENT_COMPLETE)
                        .orderPrice(OrderListInfo.getOrderPrice().get(i))
                        .product(maybeProduct.get())
                        .member(maybeMember.get())
                        .payment(payment)
                        .build();
                orderRepository.save(orderInfo);
            }
            return true;
        } catch (Exception e) {
            System.out.println("오류 발생" + e);
            return false;
        }
    }


    @Override
    public Payment savePayment(OrderForm orderForm) {
        String returnToken = validationToken.validationToken(orderForm.getToken());
        Long id = redisService.getValueByKey(returnToken);
        Member member = memberRepository.findByMemberId(id);
        MemberProfile mp = memberRepository.findProfileByMemberId(id);

        int totalOrderedCnt = 0;
        int sum = 0;
        List<ReceivedOrderItem> items = orderForm.getItems();
        for(ReceivedOrderItem item : items) {
            sum += item.getSelectedProductAmount();
            totalOrderedCnt += item.getCount();
        }

        Address address = Address.of(
                mp.getAddress().getCity(),
                mp.getAddress().getStreet(),
                mp.getAddress().getAddressDetail(),
                mp.getAddress().getZipcode()
        );

        Payment payment = Payment.
                builder()
                .paymentTitle(
                        items.size() == 1 ?
                            items.get(0).getProductName():
                            items.get(0).getProductName() + " 외 " + (items.size() - 1) + "건")
                .merchant_uid(orderForm.getMerchantUid())
                .totalPaymentPrice(sum)
                .imp_uid(orderForm.getImpUid())
                .OrderedCnt(totalOrderedCnt)
                .PaymentState(PaymentState.PAYMENT_COMPLETE)
                .address(address)
                .DeliveryRequest(orderForm.getDeliveryMessage())
                .member(member)
                .build();


        paymentRepository.save(payment);

        return payment;
    }

    @Override
    public Boolean registerOrderInfo(OrderForm orderForm) {
        try {
            Payment payment = savePayment(orderForm);
            String setOrderNum = MakeOrderedNo(payment.getMember().getId());

            for (int i = 0; i < orderForm.getItems().size(); i++) {
                Optional<Product> maybeProduct = productsRepository.findById(orderForm.getItems().get(i).getProductNo());
                Optional<Member> maybeMember = memberRepository.findById(payment.getMember().getId());

                OrderInfo orderInfo = OrderInfo
                        .builder()
                        .orderNo(setOrderNum)
                        .orderCnt(orderForm.getItems().get(i).getCount())
                        .orderState(PaymentState.PAYMENT_COMPLETE)
                        .orderPrice(orderForm.getItems().get(i).getSelectedProductAmount())
                        .product(maybeProduct.get())
                        .member(maybeMember.get())
                        .payment(payment)
                        .build();
                orderRepository.save(orderInfo);
            }
        return true;
        } catch (Exception e) {
            System.out.println("오류 발생" + e);
            return false;
        }
    }



    @Override
    public List<OrderInfo> readAllOrders(Long PaymentId) {

        List<OrderInfo> orderInfo = orderRepository.findByPaymentId(PaymentId);

        return orderInfo;
    }

    @Override
    public List<Payment> readAllPayment(String token) {
        Long id = redisService.getValueByKey(token);
        Member member = memberRepository.findByMemberId(id);
        System.out.println("맴버 번호" + member.getId());


        List<Payment> payments = paymentRepository.findAllByMemberId(member.getId());
        return payments;
    }

    @Override
    public List<Payment> readRangePaymentList(String token , String readData){
        if(token != null && token.length() > 0){
            log.info("일반회원 주문리스트 조회");
            Long id = redisService.getValueByKey(token);
            Member member = memberRepository.findByMemberId(id);
            System.out.println("맴버 번호" + member.getId());

            LocalDateTime nowData = LocalDateTime.now();
            LocalDateTime endData;

            switch (readData){
                case "3개월":
                    endData = LocalDateTime.now().minusDays(1L);
                    break;
                case "6개월":
                    endData = LocalDateTime.now().minusMonths(6L);
                    break;
                case "1년":
                    endData = LocalDateTime.now().minusYears(1L);
                    break;
                default:
                    System.out.println("해당하는 개월수가 없습니다");
                    return null;
            }
            System.out.println("startDate : " + nowData.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)")));
            System.out.println("endDate : " + endData.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)")));

            List<Payment> payments = paymentRepository.findByEndData(endData ,member.getId());
            return payments;
        } else{
            log.info("관리자회원 주문리스트 조회");
            LocalDateTime nowData = LocalDateTime.now();
            LocalDateTime endData;

            switch (readData){
                case "3개월":
                    endData = LocalDateTime.now().minusDays(1L);
                    break;
                case "6개월":
                    endData = LocalDateTime.now().minusMonths(6L);
                    break;
                case "1년":
                    endData = LocalDateTime.now().minusYears(1L);
                    break;
                default:
                    System.out.println("해당하는 개월수가 없습니다");
                    return null;
            }
            System.out.println("startDate : " + nowData.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)")));
            System.out.println("endDate : " + endData.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)")));

            List<Payment> payments = paymentRepository.findAllByEndData(endData);
            return payments;
        }
    }

    @Override
    public List<Payment> readManagerAllPayment() {
        List<Payment> payments = paymentRepository.findAllPaymentList();
        return payments;
    }

    @Override
    public Boolean refundAllOrder(RefundRequest refundRequest) throws IOException {
        String test_api_key = "2701111347244503";
        String test_api_secret = "J7xV8xenAUsYtgiuUjwAJNJ7o2Ax4VnSsaABT0G04YDwedek5x0Rzp0e1elG2od4sZTyUzVygtxUUwnp";
        IamportClient client = new IamportClient(test_api_key, test_api_secret);

        Optional<Payment> maybePayment = paymentRepository.findById(refundRequest.getRefundPaymentId());
        Payment payment = maybePayment.get();
        List<OrderInfo> orderInfos = orderRepository.findByPaymentId(payment.getPaymentId());
        String uid = payment.getImp_uid();
        System.out.println(uid);
        CancelData cancelData = new CancelData(uid, true);

        try {
            client.cancelPaymentByImpUid(cancelData);

            payment.setPaymentState(PaymentState.PAYMENT_REFUND);
            for (int i = 0; i < orderInfos.size(); i++) {
                orderInfos.get(i).setOrderState(PaymentState.PAYMENT_REFUND);
                orderInfos.get(i).setRefundReason(refundRequest.getRefundReason());
                orderRepository.save(orderInfos.get(i));
            }
            paymentRepository.save(payment);
        } catch (IamportResponseException e) {
            System.out.println("결제 취소 실패");
            System.out.println("오류 :" + e);
        }
        return true;
    }



    public Payment registerPayment(PaymentRegisterRequest paymentRegisterRequest) {

        OrderInfoRegisterForm OrderListInfo = paymentRegisterRequest.getSendInfo();
        Optional<Product> maybeProduct = productsRepository.findById(OrderListInfo.getProductID().get(0));
        Product product = maybeProduct.get();
        Integer TotalOrderedCnt = 0;
        Optional<Member> maybeMember = memberRepository.findById(OrderListInfo.getMemberID().get(0));
        for (int i = 0; i < paymentRegisterRequest.getSendInfo().getOrderCnt().size(); i++) {
            TotalOrderedCnt += paymentRegisterRequest.getSendInfo().getOrderCnt().get(i);
        }
        Address address = Address.of(
                paymentRegisterRequest.getCity(),
                paymentRegisterRequest.getStreet(),
                paymentRegisterRequest.getAddressDetail(),
                paymentRegisterRequest.getZipcode()
        );

        Payment payment = Payment.
                builder()
                .paymentTitle(
                        OrderListInfo.getProductID().size() == 1 ?
                                product.getName() :
                                product.getName() + " 외 " + (OrderListInfo.getProductID().size() - 1) + "건")
                .merchant_uid(paymentRegisterRequest.getMerchant_uid())
                .totalPaymentPrice(paymentRegisterRequest.getPaymentPrice())
                .imp_uid(paymentRegisterRequest.getImp_uid())
                .OrderedCnt(TotalOrderedCnt)
                .PaymentState(PaymentState.PAYMENT_COMPLETE)
                .address(address)
                .DeliveryRequest(paymentRegisterRequest.getSendRequest())
                .member(maybeMember.get())
                .build();


        paymentRepository.save(payment);

        return payment;
    }

    public String MakeOrderedNo(Long memberID) {
        LocalDate createdDateAt;
        createdDateAt = LocalDate.now();

        String setOrderNum;

        while (true) {
            setOrderNum = String.valueOf(setRandomOrderNo.makeIntCustomRandom(MINORDERNUM, MAXORDERNUM));
            Optional<OrderInfo> maybeOrderNo = orderRepository.findByOrderNo(setOrderNum);
            if (maybeOrderNo.isPresent()) {
                System.out.println("주문 아이디 재생성" + maybeOrderNo.get());
            } else {
                setOrderNum += "-" + createdDateAt + "-";
                setOrderNum += memberID;
                break;
            }
        }


        return setOrderNum;
    }

    public List<OrderInfo> changeOrderState(ChangeOrderStateRequest changeOrderStateRequest) {

        //배송시작, 배송완료, 구매확정, 반품신청 4개 reqType
        String reqType = changeOrderStateRequest.getReqType();
        PaymentState setState = null;
        Boolean allSameOrderStateCheck = false;
        int sameOrderStateNum = 0;
        int isDeliveryOnGoingNum = 0;
        int isWriteReviewNun = 0;

        if (reqType.equals("배송시작")) {
            setState = PaymentState.DELIVERY_ONGOING;
        } else if (reqType.equals("배송완료")) {
            setState = PaymentState.DELIVERY_COMPLETE;
        } else if (reqType.equals("구매확정")) {
            setState = PaymentState.PAYMENT_CONFIRM;
        } else if (reqType.equals("반품신청")) {
            setState = PaymentState.REFUND_REQUEST;
        } else if(reqType.equals("리뷰작성")){
            setState = PaymentState.WRITE_REVIEW;
        }

        // orderinfo는 그냥 각각 배송시작 상태 변경해주기
        Optional<OrderInfo> maybeOrderInfo = orderRepository.findById(changeOrderStateRequest.getOrderId());
        OrderInfo startOrderInfo = maybeOrderInfo.get();
        startOrderInfo.setOrderState(setState);

        orderRepository.save(startOrderInfo);

        //위에서 개별적으로 상태변경 후! 만약 if paymentid 동일한 그룹 내 -> "배송중" 상태 몇개인지 반복문 돌리기
        List<OrderInfo> findOrderInfoPaymentList = orderRepository.findByPaymentId(changeOrderStateRequest.getPaymentId());

        for (int i = 0; i < findOrderInfoPaymentList.size(); i++) {
            if (findOrderInfoPaymentList.get(i).getOrderState().equals(setState)) {
                sameOrderStateNum = sameOrderStateNum + 1;
            }
            if(findOrderInfoPaymentList.get(i).getOrderState().equals(PaymentState.DELIVERY_ONGOING)){
                isDeliveryOnGoingNum = isDeliveryOnGoingNum + 1;
            }
            if(findOrderInfoPaymentList.get(i).getOrderState().equals(PaymentState.WRITE_REVIEW)){
                isWriteReviewNun = isWriteReviewNun + 1;
            }
        }
        if (findOrderInfoPaymentList.size() == sameOrderStateNum) {
            allSameOrderStateCheck = true;
        }


        //payment - 여기는 여러개 모든 paymentId의 구성요소 orderInfo들이 다 배송시작 된 경우 payment 그룹 상태 변경 -> 배송중
        //payment 그룹 내 1개라도 배송중인 경우 부분배송 중으로 상태값 변경
        Optional<Payment> maybePayment = paymentRepository.findById(changeOrderStateRequest.getPaymentId());
        Payment payment = maybePayment.get();

        if (allSameOrderStateCheck) {
            payment.setPaymentState(setState);
        } else if(isDeliveryOnGoingNum >= 1){
            payment.setPaymentState(PaymentState.PART_DELIVERY_ONGOING);
        } else if(isWriteReviewNun >= 1){
            payment.setPaymentState(PaymentState.PART_WRITE_REVIEW);
        } else{
            payment.setPaymentState(PaymentState.ABLE_WRITE_REVIEW);
        }
        paymentRepository.save(payment);

        return findOrderInfoPaymentList;
    }

    public OrderInfo readOrder(Long orderId){
        Optional<OrderInfo> maybeOrderInfo = orderRepository.findByOrderID(orderId);
        OrderInfo orderInfo = maybeOrderInfo.get();

        return orderInfo;
    }
    public Integer salesAmount() {
        List<OrderInfo> salesOrderInfo = orderRepository.findSalesList();
        log.info("구매확정 완료된 리스트 매출");
        Integer totalSalesAmount = 0;

        for (int i = 0; i < salesOrderInfo.size(); i++) {
            totalSalesAmount = totalSalesAmount + salesOrderInfo.get(i).getOrderPrice();
        }

        return totalSalesAmount;
    }



    public List<OrderInfo> reviewWritableList(String token){
        Long id = redisService.getValueByKey(token);
        Member member = memberRepository.findByMemberId(id);
        System.out.println(member.getId());
        PaymentState paymentConfirm = PaymentState.PAYMENT_CONFIRM;
        PaymentState refundRequest = PaymentState.REFUND_REQUEST;
        List<OrderInfo> orderInfoList = orderRepository.findByPaymentState(paymentConfirm,refundRequest ,member.getId());
        return orderInfoList;
    }

}
