package com.ebanking.ClientService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CheckCustomerStatusByPhoneRequest {
    private  String  tel ;

}
