package com.ebanking.NotificationsService.model;

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
    private  Beneficiary beneficiary;
    private  Boolean sendRef ;

    private Customer customer;




}
