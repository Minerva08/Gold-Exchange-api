package com.gold.api.gold_api.user.service;

import com.gold.api.gold_api.auth.dto.JwtResponse;
import com.gold.api.gold_api.auth.enums.AuthStatus;
import com.gold.api.gold_api.global.enums.TokenType;
import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.exception.CustomException;
import com.gold.api.gold_api.global.exception.JwtAuthenticationException;
import com.gold.api.gold_api.global.utills.PasswordValidator;
import com.gold.api.gold_api.grpc.AuthServiceCaller;
import com.gold.api.gold_api.grpc.JwtServiceCaller;
import com.gold.api.gold_api.security.jwt.JwtUtill;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.entity.User;
import com.gold.api.gold_api.user.repository.UserRepository;
import com.gold.proto.JoinRequestDto;
import com.gold.proto.JoinResponseDto;
import com.gold.proto.JwtTokenReIssueResponseDto;
import com.gold.proto.RefreshTokenRequestDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final AuthServiceCaller authServiceCaller;
    private final UserRepository userRepository;
    private final JwtServiceCaller jwtServiceCaller;
    private final JwtUtill jwtUtill;


    @Autowired
    public UserServiceImpl(AuthServiceCaller authServiceCaller, UserRepository userRepository,
        JwtServiceCaller jwtServiceCaller, JwtUtill jwtUtill) {
        this.authServiceCaller = authServiceCaller;
        this.userRepository = userRepository;
        this.jwtServiceCaller = jwtServiceCaller;
        this.jwtUtill = jwtUtill;
    }


    @Override
    public JoinResponse userJoin(JoinRequest joinInfo) {


        PasswordValidator.validatePassword(joinInfo.getPassword());


        JoinRequestDto joinGrpc = JoinRequestDto.newBuilder()
            .setUserId(joinInfo.getUserId())
            .setPassword(joinInfo.getPassword())
            .setAddress(joinInfo.getAddress())
            .setBrith(joinInfo.getBirth())
            .build();

        JoinResponseDto userInfo = authServiceCaller.sendJoinUser(joinGrpc);

        User joinUserInfo = User.builder()
            .userId(userInfo.getUserId())
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
    public JwtResponse reIssueToken(String refreshToken, String userId){

        try {
            String tokenCategory = jwtUtill.getCategory(refreshToken.split("Bearer ")[1]);

            if(!tokenCategory.equals(TokenType.RT.getType())){
                log.error("[{}] Token type is not refresh. type:{}",Thread.currentThread().getStackTrace()[1].getMethodName(),tokenCategory);
                throw new JwtAuthenticationException(ErrorCode.TOKEN_TYPE_ERROR);
            }
        }catch (ExpiredJwtException e){
            throw new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED);
        }catch (Exception e){
            throw e;
        }

        User byUserId = userRepository.findByUserId(userId);

        if(byUserId==null){
            log.error("[{}] User is not exist: {}",Thread.currentThread().getStackTrace()[1].getMethodName(),userId);
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        RefreshTokenRequestDto refreshJwtGrpc = RefreshTokenRequestDto.newBuilder()
            .setRefreshToken(refreshToken)
            .setUserId(byUserId.getUserId())
            .build();

        JwtTokenReIssueResponseDto reIssueRes = jwtServiceCaller.sendRefreshToken(refreshJwtGrpc);

        if(!reIssueRes.getUserId().equals(userId)){
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

        return JwtResponse.builder()
            .userId(reIssueRes.getUserId())
            .authStatus(AuthStatus.SUCCESSFUL.name())
            .accessToken(reIssueRes.getAccessToken())
            .refreshToken(reIssueRes.getRefreshToken())
            .build();

    }
}
