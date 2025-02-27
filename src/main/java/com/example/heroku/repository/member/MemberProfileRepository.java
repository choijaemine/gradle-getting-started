package com.example.heroku.repository.member;

import com.example.heroku.entity.member.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberProfileRepository extends JpaRepository<MemberProfile,Long> {
    @Query("select mf from MemberProfile mf join fetch mf.member where mf.member.id= :memberId")
    MemberProfile findProfileByMemberId(Long memberId);
}
