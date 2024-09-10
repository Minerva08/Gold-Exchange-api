package com.gold.api.gold_api.global.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    AT("access"),
    RT("refresh");

    String type;

    TokenType(String type) {
        this.type = type;
    }
}
