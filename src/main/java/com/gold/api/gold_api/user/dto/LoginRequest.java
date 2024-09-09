package com.gold.api.gold_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드에 대한 생성자
@Builder
public class LoginRequest {
    private String userId;
    private String password;
}