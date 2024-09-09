package com.gold.api.gold_api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtRequest {
    private String jwt;

}
