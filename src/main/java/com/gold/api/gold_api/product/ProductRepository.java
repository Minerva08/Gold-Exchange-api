package com.gold.api.gold_api.product;

import com.gold.api.gold_api.global.enums.ProductCode;
import com.gold.api.gold_api.product.entity.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByProductCode(@NotNull(message = "Mandatory productCode") ProductCode productCode);
}
