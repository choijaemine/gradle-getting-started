package com.example.heroku.repository.order;


import com.example.heroku.entity.order.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long>{

    @Query("select i from Item i join fetch i.cart c join fetch i.product p where c.member.id=:memberId")
    List<Item> findCartListByMemberId(Long memberId);
    @Query("select i from Item i join fetch i.cart c join fetch i.product p where i.itemNo = :itemNo")
    Item findItemByItemNo (Long itemNo);
    @Query("select i from Item i join fetch i.cart c join fetch i.product p where p.productNo = :productNo")
    List<Item> findItemByProductNo (Long productNo);
}

