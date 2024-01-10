package com.ebanking.TransferService.external.client;

import com.ebanking.TransferService.entity.Beneficiary;
import com.ebanking.TransferService.entity.Customer;
import com.ebanking.TransferService.entity.KYC;
import com.ebanking.TransferService.entity.Wallet;
import com.ebanking.TransferService.model.AddBeneficiaryResponse;
import com.ebanking.TransferService.model.BeneficiaryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@FeignClient(name = "CLIENT-SERVICE", url = "http://192.168.0.52:8087/api/client")
public interface ExternalClientService {
    @PostMapping("/addToBlackListByCin")
    public ResponseEntity<String> addToBlackListByCin(@RequestParam String cin, @RequestParam String reason);

    @PostMapping("/addToBlackListByRib")
     ResponseEntity<String> addToBlackListByRib(@RequestParam String rib, @RequestParam String reason);
    @GetMapping("/getCustomer/{customerId}")
    Optional<Customer> getCustomerById(@PathVariable Long customerId);
    @GetMapping("getWalletById/{walletId}")
     Wallet getWalletById(@PathVariable Long walletId);
    @GetMapping("/findAllBeneficiariesByCustomerId/{customerId}")
     List<Beneficiary> getBeneficiariesByCustomerId(@PathVariable Long customerId);
    @GetMapping("findBeneficiaryById/{beneficiaryId}")
     Beneficiary getBeneficiaryById(@PathVariable Long beneficiaryId);
    @PutMapping("/{beneficiaryId}/update-transfer-id")
     ResponseEntity<Void> updateTransferIDInBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam Long transferId);
    @PutMapping("/update-balance/{customerID}")
     ResponseEntity<Void> updateWalletBalance(
            @PathVariable Long customerID,
            @RequestParam double newBalance);
    @PutMapping("/update-customer-to-customer-id/{customerID}")
     ResponseEntity<Customer> updateCustomerToCustomerID(
            @PathVariable long customerID,
            @RequestParam long customerToCustomerID);
    @PostMapping("/addBeneficiary")
     ResponseEntity<AddBeneficiaryResponse> addBeneficiary(@RequestBody BeneficiaryRequest beneficiaryRequest);

    @GetMapping("/getKycByID/{id}")
     KYC getKYCById(@PathVariable Long id);
    @GetMapping("/getWalletByWalletID/{id}")
     Wallet getWalletByWalletID(@PathVariable Long id);
    @GetMapping("findBeneficiaryByTransferID/{customerID}")
     Beneficiary getBeneficiaryByTransferId(@PathVariable Long customerID);
    @GetMapping("findCustomerByIdNumber/{idNumber}")
     Customer getCustomerByIdNumber(@PathVariable String idNumber);}
