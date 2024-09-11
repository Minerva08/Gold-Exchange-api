package com.gold.api.gold_api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gold.api.gold_api.global.error.ErrorCode;
//import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.gold.api.gold_api.user.dto.LoginResponse;
import com.google.protobuf.Descriptors;
import com.google.protobuf.util.JsonFormat;

import com.gold.api.gold_api.global.exception.CustomException;
import com.gold.api.gold_api.grpc.JwtServiceCaller;
import com.gold.api.gold_api.security.custom.CustomUserDetails;
import com.gold.api.gold_api.global.error.ErrorResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.proto.LoginRequestDto;
import com.gold.proto.LoginResponseDto;
import com.google.protobuf.util.JsonFormat;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final RequestMatcher COMPOSITE_MATCHER = new OrRequestMatcher(
        new AntPathRequestMatcher("/login", HttpMethod.POST.name())
    );

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceCaller jwtServiceCaller;

    public LoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager,
        JwtServiceCaller jwtServiceCaller) {
        super(COMPOSITE_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtServiceCaller = jwtServiceCaller;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException {
        if (!Objects.equals(request.getContentType(), CONTENT_TYPE)) {
            throw new AuthenticationServiceException(
                "지원하지 않는 Content-Type 입니다: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(),
            StandardCharsets.UTF_8);
        LoginRequest loginRequest = objectMapper.readValue(messageBody,
            LoginRequest.class);

        Set<ConstraintViolation<LoginRequest>> violations = Validation.buildDefaultValidatorFactory()
            .getValidator().validate(loginRequest);

        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            throw new AuthenticationServiceException("Validation failed: " + errorMessages);
        }

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());
        return authenticationManager.authenticate(authToken); // AuthenticationManager에 토큰 전달
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String userId = customUserDetails.getUserId();
        String password = customUserDetails.getPassword();

        try {
            //Grpc 요청
            LoginRequestDto loginGrpc = LoginRequestDto.newBuilder()
                .setUserId(userId)
                .setPassword(password)
                .build();

            LoginResponseDto loginResponseDto = jwtServiceCaller.sendLoginUser(loginGrpc);

            response.setContentType("application/json");

            // 응답의 캐릭터 인코딩 설정 (옵션)
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                objectMapper.writeValueAsString(
                    new LoginResponse(loginResponseDto.getUserId(),loginResponseDto.getAddress(),loginResponseDto.getAccessToken(),loginResponseDto.getRefreshToken())
                )

            );

        }catch (Exception e){
            log.error("[{}] User Authentication has Error :{}",Thread.currentThread().getStackTrace()[1].getMethodName(),e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(
                objectMapper.writeValueAsString(new ErrorResponse(ErrorCode.AUTHENTICATION_FAILED)));
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("[{}] User Authentication has Error :{}",Thread.currentThread().getStackTrace()[1].getMethodName(),failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
            objectMapper.writeValueAsString(new ErrorResponse(ErrorCode.AUTHENTICATION_FAILED)));
    }
}
