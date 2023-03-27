package com.example.heroku.service.tour.request;

public record ReservationRequest(

        int numberOfMember,
        String reservationDate,
        String contactNumber,
        String token,
        String foundryName) {

}
