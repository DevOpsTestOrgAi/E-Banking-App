package com.ebanking.bankTrackingService.controller;

import com.ebanking.bankTrackingService.service.BankTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/track")
@CrossOrigin(origins = "*")
public class BankTrackingServiceController {
    @Autowired
    BankTrackingService bankTrackingService;
    @PostMapping("/track-transaction")
    public ResponseEntity<?> trackTransaction(@RequestParam Long transferID,
                                              @RequestParam String idNumber) {
        bankTrackingService.addTrackedTransaction(transferID, idNumber);
        return ResponseEntity.ok("Transaction tracked successfully");
    }
}
