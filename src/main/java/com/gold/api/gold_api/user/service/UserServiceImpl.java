package com.gold.api.gold_api.user.service;

import com.gold.api.gold_api.auth.dto.JwtResponse;
import com.gold.api.gold_api.grpc.AuthServiceCaller;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.entity.User;
import com.gold.api.gold_api.user.repository.UserRepository;
import com.gold.proto.JoinRequestDto;
import com.gold.proto.JoinResponseDto;
import com.gold.proto.JwtTokenReIssueResponseDto;
import com.gold.proto.RefreshTokenRequestDto;
import com.gold.proto.RefreshTokenResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final AuthServiceCaller authServiceCaller;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(AuthServiceCaller authServiceCaller, UserRepository userRepository) {
        this.authServiceCaller = authServiceCaller;
        this.userRepository = userRepository;
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

        User joinUserInfo = User.builder()
            .userId(userInfo.getUserId())
            .address(userInfo.getUserId())
            .password(userInfo.getEncryptedPw())
            .build();

        userRepository.save(joinUserInfo);

        return JoinResponse.builder()
            .userId(userInfo.getUserId())
            .build();

    }

    /**
     * jwt 재발급  요청
     * */
    public JwtResponse reIssueToken(String expiredToken){

        RefreshTokenRequestDto refreshJwtGrpc = RefreshTokenRequestDto.newBuilder()
            .setRefreshToken(expiredToken)
            .build();

        JwtTokenReIssueResponseDto authStatusResult = authServiceCaller.expireRefreshToken(refreshJwtGrpc);

        return JwtResponse.builder().build();
    }

    /**
     * RefreshToken 사용자 인가 확인 요청 메서드
     * */
    public JwtResponse checkUserAuth(String refreshToken) {

        RefreshTokenRequestDto refreshJwtGrpc = RefreshTokenRequestDto.newBuilder()
            .setRefreshToken(refreshToken)
            .build();

        RefreshTokenResponseDto authStatusResult = authServiceCaller.sendRefreshToken(
            refreshJwtGrpc);

        return JwtResponse.builder().authStatus(authStatusResult.getAuthStatus()).build();
    }
}
