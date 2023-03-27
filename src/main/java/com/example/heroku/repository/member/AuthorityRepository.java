package com.example.heroku.repository.member;

import com.example.heroku.entity.member.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
