package com.ebanking.ClientService.external.client;

import com.ebanking.ClientService.entity.Beneficiary;
import com.ebanking.ClientService.entity.TransferEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "TRANSFER-SERVICE", url = "http://192.168.0.52:8082/api/transfers")
public interface TransferClient {
    @PostMapping("/serve/{transferID}")
     ResponseEntity<String> serveTransfer(@PathVariable Long transferID);
    @GetMapping("/getTransferByRef/{reference}")
     ResponseEntity<TransferEntity> getTransferByReference(@PathVariable String reference);
    @GetMapping("/getBeneficiaries/{transferId}")
     List<Beneficiary> getBeneficiariesByTransferId(@PathVariable Long transferId);
    @PutMapping("/{transferId}/max-pin-attempts")
     ResponseEntity<String> updateMaxPINAttempts(
            @PathVariable Long transferId,
            @RequestParam int newMaxPINAttempts
    );
    @PutMapping("/{transferId}/block")
     ResponseEntity<String> blockTransfer(@PathVariable Long transferId) ;



    }
