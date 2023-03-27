package com.example.heroku.service.member;

import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.member.MemberProfile;
import com.example.heroku.service.member.request.MemberAddressRequest;
import com.example.heroku.service.member.request.MemberLoginRequest;
import com.example.heroku.service.member.request.MemberModifyRequest;
import com.example.heroku.service.member.request.MemberRegisterRequest;


public interface MemberService {
    Boolean signUp(MemberRegisterRequest request);
    Boolean emailValidation(String email);

    Boolean managerCodeValidation(String managerCode);

    String signIn(MemberLoginRequest request);
    void withdrawal(String token);
    Member returnMemberInfo(String token);

    MemberProfile returnMemberProfile(String token);

    Boolean ModifyMemberAddress(MemberAddressRequest memberAddressRequest);

    String ModifyMemberProfile(MemberModifyRequest memberModifyRequest);

    String returnManagerProfile(String token);

}
