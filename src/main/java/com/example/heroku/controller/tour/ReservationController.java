package com.example.heroku.controller.tour;

import com.example.heroku.controller.tour.form.PaymentReservationForm;
import com.example.heroku.controller.tour.form.ReservationForm;
import com.example.heroku.entity.tour.Foundry;
import com.example.heroku.entity.tour.Reservation;
import com.example.heroku.service.tour.FoundryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("ztz/tour")
@CrossOrigin(origins = {"http://localhost:8080" , "https://tlst-ztz.web.app/"}, allowedHeaders = "*")
public class ReservationController {

    @Autowired
    FoundryService foundryservice;


    @GetMapping(path = "/list")
    public List<Foundry> foundryList() {
        return foundryservice.list();
    }

    @PostMapping(path = "/reservation")
    public String reservation(@RequestBody ReservationForm reservationForm) {
        return foundryservice.savedReservation(reservationForm.toReservationRequest());
    }

    @GetMapping(path = "/my-reservation", headers = "Token")
    public List<Reservation> myReservationList(@RequestHeader("Token") String token) {
        return foundryservice.myReservationList(token);
    }

    @DeleteMapping(path = "/my-reservation/{reservationId}", headers = "Token")
    public String cancelMyReservation (@PathVariable("reservationId") Long reservationId, @RequestHeader("Token") String token) {
        return foundryservice.cancelMyReservation(reservationId, token);
    }

    @PutMapping(path = "/my-reservation/{reservationId}")
    public String modifyMyReservation (@PathVariable("reservationId") Long reservationId, @RequestBody ReservationForm reservationForm) {
        return foundryservice.modifyMyReservation(reservationId, reservationForm.toReservationRequest());
    }


    @GetMapping(path = "/allReservationList")
    public List<Reservation> allReservationList() {
        return foundryservice.allReservationList();
    }

    @PostMapping("/my-reservation/payment")
    public String savePaymentDetail(@RequestBody PaymentReservationForm paymentReservationForm){
        log.info("savePaymentDetail" + paymentReservationForm);

        return foundryservice.saveMyReservationPaymentDetail(paymentReservationForm.toPaymentReservationRequest()) ;
    }

}
