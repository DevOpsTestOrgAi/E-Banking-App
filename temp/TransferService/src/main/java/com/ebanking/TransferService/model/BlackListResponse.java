package com.ebanking.TransferService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder



public class BlackListResponse {
    private Boolean isBlocked;
}
