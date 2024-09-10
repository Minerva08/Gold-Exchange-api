package com.gold.api.gold_api.global.enums;

import lombok.Getter;

@Getter
public enum ProductCode {
    GOLD_99_9("금 99.9%"), GOLD_99_99("금 99.99%");

    private String name;
    private String code;

    ProductCode(String name) {
        this.name = name;
        this.code = String.valueOf(ProductCode.valueOf(name));
    }
}
