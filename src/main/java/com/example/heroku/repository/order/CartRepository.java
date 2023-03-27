package com.example.heroku.repository.order;

import com.example.heroku.entity.order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberId(Long memberId);

    @Query("select c from Cart c join fetch c.member m where c.member.id = :id")
    Cart findCartByMemberId(Long id);
}