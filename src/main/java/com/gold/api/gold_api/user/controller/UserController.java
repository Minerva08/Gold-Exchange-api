package com.gold.api.gold_api.user.controller;

import com.gold.api.gold_api.auth.dto.JwtResponse;
import com.gold.api.gold_api.global.CommonResponse;
import com.gold.api.gold_api.user.dto.JoinRequest;
import com.gold.api.gold_api.user.dto.JoinResponse;
import com.gold.api.gold_api.user.dto.LoginRequest;
import com.gold.api.gold_api.user.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> login( @RequestBody LoginRequest loginInfo){
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @GetMapping("/reissue-token")
    public ResponseEntity<CommonResponse<JwtResponse>> reIssueJwt(@RequestHeader(value="Authorization") String auth, @RequestParam(value = "userId") String userId){

        CommonResponse<JwtResponse> response = CommonResponse.ok("토큰 재발급에 성공하였습니다.",
            userService.reIssueToken(auth,userId));

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

}
