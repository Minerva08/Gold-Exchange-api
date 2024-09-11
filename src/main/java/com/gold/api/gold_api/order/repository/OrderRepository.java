package com.gold.api.gold_api.order.repository;

import com.gold.api.gold_api.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Invoice,Long> {


}
