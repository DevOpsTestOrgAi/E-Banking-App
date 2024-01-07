package com.ebanking.TransferService.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransferHistoriesResponse {
    private String type ;
    private String state  ;
    private double transferedAmount;
    private Long taransferID  ;
    private String initatedDate  ;
    private List<Recipient> recipient;
    private Sender sender;
    private String message ;
    private Boolean isFound ;




}
