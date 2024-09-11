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
public class UpdateOrderResponse {
    private String orderNum;
    private DealStatus dealStatus;

}
