package com.example.heroku.entity.order;

import jakarta.persistence.*;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.products.Product;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;
    @Column(nullable = false)
    private String orderNo;
    @Column(nullable = false)
    private Integer orderCnt;

    @Column
    private Integer orderPrice;

    @Column(nullable = false)
    private PaymentState orderState;

    @Column
    private String refundReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_Id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Payment_id")
    private Payment payment;


    @CreationTimestamp
    private Date regDate;

    @UpdateTimestamp
    private Date updDate;

    public void ModifyOrderCnt(Integer ModifyCnt){
        this.orderCnt = ModifyCnt;
    }

}
