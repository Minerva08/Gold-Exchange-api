package com.gold.api.gold_api.invoice.entity;

import com.gold.api.gold_api.global.enums.DealStatus;
import com.gold.api.gold_api.log.entity.BusinessLog;
import com.gold.api.gold_api.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="invoice_id")
    private Long invoiceId;
    private String orderNum;
    private String invoiceType;
    private String deliveryAddress;
    private float productCnt;
    @Enumerated(EnumType.STRING)
    private DealStatus status;
    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime updatedAt= LocalDateTime.now();


    @OneToMany(mappedBy = "invoice")
    @Builder.Default
    private List<InvoiceProduct> invoiceProductList = new ArrayList<>();

    @OneToMany(mappedBy = "invoice",cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<BusinessLog> businessLogList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private User user;

    public void addInvoiceStep(){
        BusinessLog log = BusinessLog.builder()
            .status(this.getStatus())
            .createDateAt(this.getUpdatedAt())
            .user(this.getUser())
            .invoice(this)
            .build();
       this.businessLogList.add(log);
    }
}
