package com.gold.api.gold_api.global.enums;

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

    DealStatus(String type, String name, String code) {
        this.type = type;
        this.name = name;
        this.code = code;
    }
}
