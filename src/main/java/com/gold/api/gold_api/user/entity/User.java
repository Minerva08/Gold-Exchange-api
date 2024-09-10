package com.gold.api.gold_api.user.entity;

import com.gold.api.gold_api.invoice.entity.Invoice;
import com.gold.api.gold_api.log.entity.BusinessLog;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String userId;
    private String address;
    private String password;
    @Builder.Default
    private LocalTime last_login_at=LocalTime.now();

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Invoice> invoiceList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<BusinessLog> businessLogList = new ArrayList<>();

}
