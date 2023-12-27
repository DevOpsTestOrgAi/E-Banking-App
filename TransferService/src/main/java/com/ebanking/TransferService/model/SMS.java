package com.ebanking.TransferService.model;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SMS {
    private TransferType transferType;

    private  double  amount  ;
    private  String pin ;
    private  String ref ;
    private List<Beneficiary> beneficiaries;
    private  Boolean sendRef ;

    private Customer customer;




}
