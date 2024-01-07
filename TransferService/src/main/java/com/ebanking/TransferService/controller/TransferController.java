package com.ebanking.TransferService.controller;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.model.RestitutionTransferResponse;
import com.ebanking.TransferService.model.TransferHistoriesResponse;
import com.ebanking.TransferService.model.TransferRequest;
import com.ebanking.TransferService.model.TransferResponse;
import com.ebanking.TransferService.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<TransferResponse> initiateTransfer(@RequestBody TransferRequest transferRequest) throws IOException {

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
    @PutMapping("/{transferId}/unblock")
    public ResponseEntity<String> unblockTransfer(@PathVariable Long transferId) {

        transferService.unblockTransfer(transferId);
        return ResponseEntity.ok("Transfer is unblocked successfully");
    }
    @GetMapping("/downloadPDF/{transferId}")
    public ResponseEntity<ByteArrayResource> downloadTransferReceipt(@PathVariable Long transferId) {
        // Get the PDF bytes from the service
        byte[] pdfBytes = transferService.getTransferReceipt(transferId);

        if (pdfBytes != null) {
            // Create a ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transfer-receipt.pdf");

            // Return ResponseEntity with ByteArrayResource, headers, and content type
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            // Handle the case when PDF bytes are not available or TransferEntity is not found
            // For example, return an appropriate error response
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/restitute/{transferId}")
    public ResponseEntity<RestitutionTransferResponse> restitutionTransfer(@PathVariable Long transferId) {
        RestitutionTransferResponse response = transferService.restitutionTransfer(transferId);

            return ResponseEntity.ok(response);

    }
    @GetMapping("/getTransferHistory/{idNumber}")
    public ResponseEntity<List<TransferHistoriesResponse>> getTransferHistoriesByCustomerId(
            @PathVariable String idNumber) {
        try {
            List<TransferHistoriesResponse> histories = transferService.getTransferHistoriesByCustomerIdNumber(idNumber);
            if (histories.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            // Log the exception and return an appropriate error response
            return ResponseEntity.internalServerError().body(null); // Update as needed based on your error handling strategy
        }
    }

}