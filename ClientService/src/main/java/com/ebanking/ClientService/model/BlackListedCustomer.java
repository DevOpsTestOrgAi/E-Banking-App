package com.ebanking.ClientService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlackListedCustomer {
    private Long id ;

    private String fullName ;
    private String rib  ;
    private String cin;
    private String reasonToAddtoBlackList ;

}
