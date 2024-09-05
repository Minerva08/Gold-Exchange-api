package com.gold.api.gold_api.user.service;

import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.api.gold_api.user.dto.LoginResponse;
import com.gold.proto.LoginRequestDto;

public interface UserService {

    LoginResponse userLogin(LoginRequest loginRequest);


    JoinResponse userJoin(JoinRequest joinInfo);
}
