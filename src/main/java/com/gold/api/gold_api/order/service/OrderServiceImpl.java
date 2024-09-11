package com.gold.api.gold_api.order.service;

import com.gold.api.gold_api.global.enums.DealStatus;
import com.gold.api.gold_api.global.enums.ProductCode;
import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.exception.CustomException;
import com.gold.api.gold_api.invoice.InvoiceProductRepository;
import com.gold.api.gold_api.invoice.entity.Invoice;
import com.gold.api.gold_api.log.BusinessLogRepository;
import com.gold.api.gold_api.order.dto.PurchaseOrderRequest;
import com.gold.api.gold_api.order.dto.PurchaseOrderResponse;
import com.gold.api.gold_api.order.dto.UpdateOrderResponse;
import com.gold.api.gold_api.order.repository.OrderRepository;
import com.gold.api.gold_api.product.ProductRepository;
import com.gold.api.gold_api.product.entity.Product;
import com.gold.api.gold_api.user.entity.User;
import com.gold.api.gold_api.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly=true)
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PurchaseOrderResponse registerPurchase(String orderType,PurchaseOrderRequest request, String userId) {

        Invoice mappedInvoice = convertInvoiceEntity(orderType,userId, request);
        mappedInvoice.addInvoiceStep();

        Invoice registInvoice = orderRepository.save(mappedInvoice);

        PurchaseOrderResponse purchaseOrder = PurchaseOrderResponse.builder()
            .userId(userId)
            .orderNum(registInvoice.getOrderNum())
            .productCode(request.getOrderInfo().getProductCode())
            .productName(ProductCode.fromCode(request.getOrderInfo().getProductCode()).getName())
            .purchaseCnt(registInvoice.getProductCnt())
            .dealStatus(registInvoice.getStatus().getCode())
            .build();

        return purchaseOrder;
    }

    @Override
    public UpdateOrderResponse updateOrderStatus(String ordernum, String modeStatus,
        String userId) {

        LocalDateTime update = LocalDateTime.now();
        orderRepository.updateOrderStatus(DealStatus.fromCode(modeStatus),ordernum,update);

        return UpdateOrderResponse.builder()
            .dealStatus(DealStatus.fromCode(modeStatus))
            .orderNum(ordernum)
            .build();
    }


    private Invoice convertInvoiceEntity(String orderType,String userId, PurchaseOrderRequest purchaseInfo){

        Product byProductCode = productRepository.findByProductCode(ProductCode.fromCode(purchaseInfo.getOrderInfo().getProductCode()));

        if(byProductCode==null){
            log.error("[{}] Product is null",Thread.currentThread().getStackTrace()[1].getMethodName());
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        User userInfo = userRepository.findByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createDate = LocalDateTime.parse(purchaseInfo.getPurchaseDate(), formatter);

        StringBuilder orderNum = new StringBuilder()
            .append(String.valueOf(orderType.toCharArray()[0]).toUpperCase())
            .append("_")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        Invoice convertInvoice = Invoice.builder()
            .user(userInfo)
            .orderNum(orderNum.toString())
            .status(orderType.equals("purchase")?DealStatus.P_ORDER_COMP:DealStatus.S_ORDER_COMP)
            .invoiceType(orderType.toLowerCase())
            .deliveryAddress(purchaseInfo.getAddress())
            .productCnt(Float.parseFloat(purchaseInfo.getOrderInfo().getProductCnt()))
            .createdAt(createDate)
            .build();


        byProductCode.addInvoice(convertInvoice);

        return convertInvoice;

    }

}
