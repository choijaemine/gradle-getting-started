package com.example.heroku.entity.products;

import jakarta.persistence.*;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.order.OrderInfo;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private String content;

    @Column
    private String thumbnailFileName;

    @CreationTimestamp
    private LocalDate regDate;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "product_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "order_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderInfo orderInfo;
}
