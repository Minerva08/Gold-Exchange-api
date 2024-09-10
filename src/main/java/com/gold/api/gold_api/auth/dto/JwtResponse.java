package com.gold.api.gold_api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String authStatus;

}
