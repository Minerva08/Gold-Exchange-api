package com.gold.api.gold_api.user.service;

import com.gold.api.gold_api.auth.dto.JwtResponse;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.api.gold_api.user.dto.LoginResponse;
import com.gold.proto.LoginRequestDto;

public interface UserService {

    JoinResponse userJoin(JoinRequest joinInfo);


    JwtResponse reIssueToken(String refreshToken, String userId);
}
