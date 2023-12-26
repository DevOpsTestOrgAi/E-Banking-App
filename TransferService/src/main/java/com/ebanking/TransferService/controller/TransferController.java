package com.ebanking.TransferService.controller;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.model.TransferRequest;
import com.ebanking.TransferService.model.TransferResponse;
import com.ebanking.TransferService.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/Test")
    public String test() {

        return "Hello zaid from transfer";
    }

    @PostMapping("/initiate")
    public ResponseEntity<TransferResponse> initiateTransfer(@RequestBody TransferRequest transferRequest) throws JsonProcessingException {

        return ResponseEntity.ok(transferService.initiateTransfer(transferRequest));
    }
    @PostMapping("/serve/{transferID}")
    public ResponseEntity<String> serveTransfer(@PathVariable Long transferID) {
        transferService.serveTransfer(transferID);
        return ResponseEntity.ok("Transfer served successfully.");
    }
    @GetMapping("/getTransferByRef/{reference}")
    public ResponseEntity<TransferEntity> getTransferByReference(@PathVariable String reference) {
        Optional<TransferEntity> transfer = transferService.getTransferByRef(reference);

        return transfer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/getBeneficiaries/{transferId}")
    public List<Beneficiary> getBeneficiariesByTransferId(@PathVariable Long transferId) {
        return transferService.getAllBeneficiariesByTransferId(transferId);
    }
    @PutMapping("/{transferId}/max-pin-attempts")
    public ResponseEntity<String> updateMaxPINAttempts(
            @PathVariable Long transferId,
            @RequestParam int newMaxPINAttempts
    ) {
        transferService.updateMaxPINAttempts(transferId,newMaxPINAttempts);
        return new ResponseEntity<>("PIN Attempt Updated ",HttpStatus.OK);

    }
    @PutMapping("/{transferId}/block")
    public ResponseEntity<String> blockTransfer(@PathVariable Long transferId) {

        transferService.blockTransfer(transferId);
        return ResponseEntity.ok("Transfer is blocked successfully");
    }

}