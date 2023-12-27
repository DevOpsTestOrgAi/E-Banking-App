package com.ebanking.TransferService.entity;

import com.ebanking.TransferService.model.TransferState;
import com.ebanking.TransferService.model.TransferType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties("beneficiaries")
public class TransferEntity {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private double amount;
    private double maxBAmountPeriodC;
    private int maxTransfersPerCustomer;
    private String toBeServedFrom;
    private String PINCode;
    private int maxPIN_Attempts;

    private int validationDuration;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<Beneficiary> beneficiaries;
    private  String motif;
    @Enumerated(EnumType.STRING)
    private TransferState state;
    @Enumerated(EnumType.STRING)
    private TransferType type;




}