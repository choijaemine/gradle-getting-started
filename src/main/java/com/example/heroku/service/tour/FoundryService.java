package com.example.heroku.service.tour;

import com.example.heroku.entity.tour.Foundry;
import com.example.heroku.entity.tour.Reservation;
import com.example.heroku.service.tour.request.PaymentReservationRequest;
import com.example.heroku.service.tour.request.ReservationRequest;

import java.util.List;

public interface FoundryService {
    List<Foundry> list();
    String savedReservation(ReservationRequest reservationRequest);
    List<Reservation> myReservationList(String token);
    String cancelMyReservation(Long reservationId, String token);
    String modifyMyReservation(Long reservationId, ReservationRequest reservationRequest);
    List<Reservation> allReservationList();
    String saveMyReservationPaymentDetail(PaymentReservationRequest paymentReservationRequest);

}
