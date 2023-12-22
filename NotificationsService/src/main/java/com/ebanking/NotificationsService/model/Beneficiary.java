package com.ebanking.NotificationsService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Beneficiary {
     private String firstName  ;
     private String lastName ;
      private String phone  ;


}
