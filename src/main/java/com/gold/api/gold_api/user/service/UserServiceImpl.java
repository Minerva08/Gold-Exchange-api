package com.gold.api.gold_api.user.service;

import com.gold.api.gold_api.grpc.AuthServiceCaller;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.api.gold_api.user.dto.LoginResponse;
import com.gold.api.gold_api.user.repository.UserRepository;
import com.gold.proto.JoinRequestDto;
import com.gold.proto.JoinResponseDto;
import com.gold.proto.LoginRequestDto;
import com.gold.proto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final AuthServiceCaller authServiceCaller;

    @Autowired
    public UserServiceImpl(AuthServiceCaller authServiceCaller) {
        this.authServiceCaller = authServiceCaller;
    }

    public LoginResponse userLogin(LoginRequest loginRequest){
        LoginRequestDto loginGrpc = LoginRequestDto.newBuilder()
            .setUserId(loginRequest.getUserId())
            .setPassword(loginRequest.getPassword())
            .build();

        LoginResponseDto loginInfo = authServiceCaller.sendLoginInfo(loginGrpc);


        return LoginResponse.builder()
            .userId(loginRequest.getUserId())
            .accessToken(loginInfo.getAccessToken())
            .refreshToken(loginInfo.getRefreshToken())
            .build();
    }

    @Override
    public JoinResponse userJoin(JoinRequest joinInfo) {

        JoinRequestDto joinGrpc = JoinRequestDto.newBuilder()
            .setUserId(joinInfo.getUserId())
            .setPassword(joinInfo.getPassword())
            .setAddress(joinInfo.getAddress())
            .setBrith(joinInfo.getBirth())
            .build();


        JoinResponseDto userInfo = authServiceCaller.sendJoinUser(joinGrpc);

        return JoinResponse.builder()
            .userId(userInfo.getUserId())
            .build();

    }
}
