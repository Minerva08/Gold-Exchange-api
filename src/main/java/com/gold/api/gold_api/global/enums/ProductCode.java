package com.gold.api.gold_api.global.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum ProductCode {
    GOLD_99_9("금 99.9%", "GOLD_99_9"),
    GOLD_99_99("금 99.99%", "GOLD_99_99");

    private final String name;
    private final String code;

    private static final Map<String, ProductCode> CODE_TO_ENUM = new HashMap<>();
    private static final Map<String, ProductCode> NAME_TO_ENUM = new HashMap<>();

    // 생성자
    ProductCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // code로 ProductCode 찾기
    public static ProductCode fromCode(String code) {
        for (ProductCode productCode : ProductCode.values()) {
            if (productCode.getCode().equals(code)) {
                return productCode;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    static {
        for (ProductCode productCode : values()) {
            CODE_TO_ENUM.put(productCode.getCode(), productCode);
            NAME_TO_ENUM.put(productCode.getName(), productCode);
        }
    }

    public static String getNameByCode(String code) {
        ProductCode productCode = CODE_TO_ENUM.get(code);
        return (productCode != null) ? productCode.getName() : null;
    }

    public static String getCodeByName(String name) {
        ProductCode productCode = NAME_TO_ENUM.get(name);
        return (productCode != null) ? productCode.getCode() : null;
    }
}
