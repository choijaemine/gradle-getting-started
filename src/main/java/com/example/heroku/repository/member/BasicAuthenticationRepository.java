package com.example.heroku.repository.member;

import com.example.heroku.entity.member.BasicAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BasicAuthenticationRepository extends JpaRepository<BasicAuthentication, Long> {
    @Query("select b from BasicAuthentication b where b.member.id = :memberID")
    Optional<BasicAuthentication> findByMemberID(Long memberID);
}
