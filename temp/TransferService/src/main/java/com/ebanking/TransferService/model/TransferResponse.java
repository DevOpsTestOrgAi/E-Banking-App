package com.ebanking.TransferService.model;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.SecondaryTable;

@Builder

@AllArgsConstructor

@NoArgsConstructor
@Data
public class TransferResponse {
    private String  message  ;
    private  Boolean isInitaited;
   private Long transferID;
   private String ref  ;


}
