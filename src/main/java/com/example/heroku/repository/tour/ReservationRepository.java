package com.example.heroku.repository.tour;

import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.tour.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository  extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where r.member = :member")
    List<Reservation> findByMember(Member member);

    @Query("select r from Reservation r where r.member.id = :memberId")
    List<Reservation> findByMemberId(Long memberId);
    @Query("select r from Reservation r where r.ReservationId = :ReservationId")
    Reservation findByReservationId(Long ReservationId);
}

