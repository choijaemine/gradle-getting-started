package com.example.heroku.controller.tour.form;

import com.example.heroku.service.tour.request.ReservationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationForm {

    private int numberOfMember;
    private String reservationDate;
    private String contactNumber;
    private String token;
    private String foundryName;


    public ReservationRequest toReservationRequest(){
        return new ReservationRequest(numberOfMember,reservationDate,contactNumber,token,foundryName);
    }
}
