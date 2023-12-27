package com.ebanking.TransferService.model;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.Customer;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    private String reference;
    private double amount;
    private double maxBAmountPeriodC;
    private int maxTransfersPerCustomer;
    private String toBeServedFrom;
    private String PINCode;
    private int validationDuration;
    private Customer customer; // Assuming you want to reference the customer by ID
    private List<Beneficiary> beneficiaries;
    private String motif;
    private TransferState state;
    private TransferType type;

    // Constructors, getters, setters...
}