package com.gold.api.gold_api.user.controller;

import com.gold.api.gold_api.global.CommonResponse;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.api.gold_api.user.dto.LoginResponse;
import com.gold.api.gold_api.user.service.UserService;

import jakarta.servlet.ServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<CommonResponse<JoinResponse>> join(@RequestBody JoinRequest joinInfo){
        JoinResponse joinResponse = userService.userJoin(joinInfo);
        CommonResponse<JoinResponse> res = CommonResponse.ok("회원가입 성공",joinResponse);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(ServletResponse response,  @RequestBody LoginRequest loginInfo){
//        CommonResponse<LoginResponse> res = CommonResponse.ok("로그인 성공",response.get);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
