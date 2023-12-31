package com.ebanking.ClientService.model;

import com.ebanking.ClientService.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindCustomerByPhoneResponse {
    private Customer customer ;
     private String  message ;
     private Long id ;
     private  Boolean  isBlockedOrExist;

}
