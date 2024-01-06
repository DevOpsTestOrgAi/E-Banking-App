package com.ebanking.TransferService.model;

import com.ebanking.TransferService.entity.Beneficiary;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    private String reference;
    private List<Double> amounts;
    private double amount;
    private double maxBAmountPeriodC;
    private int maxTransfersPerCustomer;
    private String toBeServedFrom;
    private String pin;
    private int validationDuration;
    private  BeneficiaryRequest beneficiaryRequest;
    private Long customerID;
//    private List<Beneficiary> beneficiaries;
    private String motif;
    private TransferState state;
    private TransferType type;
    private String otp;
    private  Boolean isNotificationsSendingChosen;
    private FeeType feeType;
    private List<Long> beneficiaries_ids;
    private Boolean isInitiatedFromMobile ;


    // Constructors, getters, setters...
}