package com.example.heroku.service.member.request;

import com.example.heroku.entity.member.Authority;
import com.example.heroku.entity.member.AuthorityType;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.member.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MemberRegisterRequest {

    private final String email;
    private final String password;

    private final String username;
    private final int birthdate;

    private final AuthorityType authorityName;

    private boolean managerCheck;
    private String managerCode;
    private String city;
    private String street;
    private String addressDetail;
    private String zipcode;
    private final String phoneNumber;

    public MemberRegisterRequest(String email, String password, String username, int birthdate, AuthorityType authorityName, boolean managerCheck, String city, String street, String addressDetail, String zipcode, String phoneNumber){
        this.email= email;
        this.password = password;
        this.username= username;
        this.birthdate = birthdate;
//        this.authorityNo = authorityNo;
        this.authorityName = authorityName;
        this.managerCheck = managerCheck;
        this.city= city;
        this.street = street;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
    }

    public MemberRegisterRequest(String email, String password, String username, int birthdate, AuthorityType authorityName,boolean managerCheck, String phoneNumber){
        this.email = email;
        this.password = password;
        this.username= username;
        this.birthdate = birthdate;
//        this.authorityNo = authorityNo;
        this.authorityName = authorityName;
        this.managerCheck = managerCheck;
        this.phoneNumber =phoneNumber;
    }

    public Member toMember () {
        return new Member(
                email,
                username,
                birthdate,
                Authority.ofMember(authorityName),
                managerCheck,
                MemberProfile.of(city, street, addressDetail, zipcode,phoneNumber)
        );
    }

    public Member toManager() {
        return new Member(
                email,
                username,
                birthdate,
                Authority.ofMember(authorityName),
                managerCheck
        );
    }

}
