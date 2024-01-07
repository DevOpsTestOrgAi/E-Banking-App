package com.ebanking.ClientService.model;

import com.ebanking.ClientService.entity.KYC;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindKYCResponse {
    private boolean isFound;
    private String message;
    private KYC kyc;
    private  Boolean isExpired ;


}
