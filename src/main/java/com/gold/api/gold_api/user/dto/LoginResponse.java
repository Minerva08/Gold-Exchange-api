package com.gold.api.gold_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;

}
