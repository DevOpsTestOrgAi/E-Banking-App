package com.ebanking.ClientService.model;

import com.ebanking.ClientService.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddKYCResponse {
    private String message ;
    private Boolean isAdded;
    private Customer customer;

}
