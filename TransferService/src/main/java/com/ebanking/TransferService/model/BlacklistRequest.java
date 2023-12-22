package com.ebanking.TransferService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlacklistRequest {
    private String cin;
    private String rib ;

    private String reason;
}