package com.ebanking.TransferService.model;

import com.ebanking.TransferService.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestitutionTransferResponse {
    private String message ;
    private  Boolean isRestitutive;
    private List<Object> customerList;

}
