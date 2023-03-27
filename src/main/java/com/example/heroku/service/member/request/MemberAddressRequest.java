package com.example.heroku.service.member.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Getter
@ToString
@RequiredArgsConstructor
public class MemberAddressRequest {
    private final String city;
    private final String street;
    private final String addressDetail;
    private final String zipcode;
    private final String token;

}
