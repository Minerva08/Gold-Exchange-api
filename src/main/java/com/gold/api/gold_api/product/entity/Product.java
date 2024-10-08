package com.gold.api.gold_api.product.entity;

import com.gold.api.gold_api.global.enums.ProductCode;
import com.gold.api.gold_api.invoice.entity.Invoice;
import com.gold.api.gold_api.invoice.entity.InvoiceProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long productId;
    @Enumerated(EnumType.STRING)
    private ProductCode productCode;
    private String productName;
    private LocalTime createdAt;

    @OneToMany(mappedBy = "product" ,cascade = CascadeType.ALL)
    @Builder.Default
    private List<InvoiceProduct> invoiceProductList = new ArrayList<>();

    public void addInvoice(Invoice invoice) {
        InvoiceProduct invoiceProduct = InvoiceProduct.builder().invoice(invoice).product(this).build();
        this.invoiceProductList.add(invoiceProduct);
    }
}
