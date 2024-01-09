package com.ebanking.TransferService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder

@AllArgsConstructor

@NoArgsConstructor
@Data
public class GetAllTransfersStatistics {
    private Long  total ;
    private Long servedNumber;
    private Long restituedNumber;
    private Long blockedNumber ;
    private Long initiatedNumber;

}
