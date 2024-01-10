package com.ebanking.ClientService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCustomerToBlackListRequest {
    private String cin ;
    private String rib ;
    private String reason  ;
    private  String firstName ;
    private String lastName ;

}
