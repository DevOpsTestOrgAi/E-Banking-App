package com.ebanking.TransferService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BANK-TRACKING-SERVICE", url = "http://bank-tracking-service-svc/track")
public interface ExternalTrackingService {
    @PostMapping("/track-transaction")
     ResponseEntity<?> trackTransaction(@RequestParam Long transferID,
                                              @RequestParam String idNumber);
}
