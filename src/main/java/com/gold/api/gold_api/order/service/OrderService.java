package com.gold.api.gold_api.order.service;

import com.gold.api.gold_api.global.CommonResponse;
import com.gold.api.gold_api.order.dto.PurchaseOrderRequest;
import com.gold.api.gold_api.order.dto.PurchaseOrderResponse;
import com.gold.api.gold_api.order.dto.UpdateOrderResponse;

public interface OrderService {

    PurchaseOrderResponse registerPurchase(String orderType, PurchaseOrderRequest request, String userId, String address);

    UpdateOrderResponse updateOrderStatus(String ordernum,String modeStatus, String userId);
}
