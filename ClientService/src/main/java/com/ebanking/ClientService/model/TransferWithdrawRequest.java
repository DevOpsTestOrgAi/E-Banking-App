package com.ebanking.ClientService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransferWithdrawRequest {
     private TransferType transferType;
     private  String   pin ;
     private   String  ref  ;
   private  String   cin ;



}
