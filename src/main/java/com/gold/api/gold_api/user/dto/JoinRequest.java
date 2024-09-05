package com.gold.api.gold_api.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    private String userId;
    private String password;
    private String birth;
    private String address;

}
