package com.ebanking.ClientService.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BeneficiaryRequest {
    private Long customerID;
    private String firstName;
    private String lastName;
    private String phone;
    private String rib;
    private String cin;
}
