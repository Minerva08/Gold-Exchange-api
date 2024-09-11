package com.gold.api.gold_api.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class PurchaseOrderRequest {

    private ProductInfo orderInfo;
    @Nullable
    private String purchaseDate;
    @Nullable
    private String address;

    @Getter
    @Setter
    @Validated
    public class ProductInfo {

        @Pattern(regexp = "GOLD_99_9|GOLD_99_99", message = "Invalid productCode")
        @NotNull(message = "Mandatory productCode")
        private String productCode;
        @NotNull(message = "Mandatory purchaseCnt")
        private String productCnt;
    }

}
