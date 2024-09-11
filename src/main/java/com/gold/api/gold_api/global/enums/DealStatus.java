package com.gold.api.gold_api.global.enums;


import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum DealStatus {
    P_ORDER_COMP("Purchase","주문완료","POC"),
    P_PAY_COMP("Purchase","입금완료","PPC"),
    P_DELIVER_COMP("Purchase","발송완료","PDC"),
    S_ORDER_COMP("Sale","주문완료","SOC"),
    S_PAY_COMP("Sale","송금완료","SPC"),
    S_DELIVER_COMP("Sale","수령완료","SDC"),
    ;

    private final String type;
    private final String name;
    private final String code;

    private static final Map<String, DealStatus> CODE_TO_ENUM = new HashMap<>();
    private static final Map<String, DealStatus> NAME_TO_ENUM = new HashMap<>();


    DealStatus(String type, String name, String code) {
        this.type = type;
        this.name = name;
        this.code = code;
    }

    static {
        for (DealStatus dealStatus : values()) {
            CODE_TO_ENUM.put(dealStatus.getCode(), dealStatus);
            NAME_TO_ENUM.put(dealStatus.getName(), dealStatus);
        }
    }

    // code로 ProductCode 찾기
    public static DealStatus fromCode(String code) {
        for (DealStatus dealStatus : DealStatus.values()) {
            if (dealStatus.getCode().equals(code)) {
                return dealStatus;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    public static String getNameByCode(String code) {
        DealStatus dealStatus = CODE_TO_ENUM.get(code);
        return (dealStatus != null) ? dealStatus.getName() : null;
    }

    public static String getCodeByName(String name) {
        DealStatus dealStatus = NAME_TO_ENUM.get(name);
        return (dealStatus != null) ? dealStatus.getCode() : null;
    }
}
