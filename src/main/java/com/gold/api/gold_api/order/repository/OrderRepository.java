package com.gold.api.gold_api.order.repository;

import com.gold.api.gold_api.global.enums.DealStatus;
import com.gold.api.gold_api.invoice.entity.Invoice;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Invoice,Long> {


    Invoice findByOrderNum(String ordernum);

    @Modifying
    @Query("UPDATE Invoice i SET i.status = :modeStatus , i.updatedAt = :updateTime WHERE i.orderNum = :orderNum")
    void updateOrderStatus(@Param("modeStatus") DealStatus modeStatus, @Param("orderNum") String orderNum, @Param("updateTime") LocalDateTime updateTime);
}
