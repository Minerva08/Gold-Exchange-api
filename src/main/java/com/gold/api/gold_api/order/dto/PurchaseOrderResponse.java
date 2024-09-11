package com.gold.api.gold_api.order.dto;

import com.gold.api.gold_api.global.enums.DealStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PurchaseOrderResponse {
    private String orderNum;
    private String userId;
    private String productCode;
    private String productName;
    private float purchaseCnt;
    private DealStatus dealStatus;

}
