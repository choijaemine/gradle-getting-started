package com.example.heroku.repository.member;


import com.example.heroku.entity.member.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    @Query("select a from Authentication a join a.member where a.member.id = :memberId")
    Optional<Authentication> findByMemberId(Long memberId);
}
