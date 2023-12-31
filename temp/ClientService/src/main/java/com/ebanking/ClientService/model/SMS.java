package com.ebanking.ClientService.model;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SMS {
    private TransferType transferType;

    private  double  amount  ;
    private  String pin ;
    private  String ref ;
    private Beneficiary beneficiary;
    private  Boolean sendRef ;

    private Customer customer;




}
