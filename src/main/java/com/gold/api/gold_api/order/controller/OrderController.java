package com.gold.api.gold_api.order.controller;

import com.gold.api.gold_api.global.CommonResponse;
import com.gold.api.gold_api.order.dto.PurchaseOrderRequest;
import com.gold.api.gold_api.order.dto.PurchaseOrderResponse;
import com.gold.api.gold_api.order.service.OrderService;
import com.gold.api.gold_api.security.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @Operation(summary = "구매 요청", description = "소비자 금 구매 요청")
    @PostMapping(value = "/{orderType}", produces = "application/json")
    public ResponseEntity<PurchaseOrderResponse> createPurchase(
        @RequestBody PurchaseOrderRequest request,
        @PathVariable(value = "orderType") @Pattern(regexp = "purchase|sale") String orderType,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
        ){

        PurchaseOrderResponse response = orderService.registerPurchase(orderType,request, customUserDetails.getUserId());

        return ResponseEntity.ok(response);
    }

}
