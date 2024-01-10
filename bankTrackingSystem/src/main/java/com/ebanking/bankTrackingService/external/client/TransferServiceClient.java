package com.ebanking.bankTrackingService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "TRANSFER-SERVICE", url = "http://transfer-service-svc/api/transfers")

public interface TransferServiceClient {
    @PutMapping("/{transferId}/block")
     ResponseEntity<String> blockTransfer(@PathVariable Long transferId);
}
