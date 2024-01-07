package com.ebanking.ClientService.controller;


import com.ebanking.ClientService.entity.*;
import com.ebanking.ClientService.model.*;
import com.ebanking.ClientService.service.ClientManagementServiceImpl;
import com.ebanking.ClientService.service.ClientTransferOperationServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "*")
public class ClientManagementController {

    @Autowired
    private ClientManagementServiceImpl clientManagementService;
    @Autowired
    ClientTransferOperationServiceImpl clientTransferOperationService;
//    @Autowired
//    private  Consumer consumer;


//    @GetMapping("/getAllFromTopic")
//    public List<Object> consumeAndGetObjects() {
//
//        return consumer.getAllReceivedObjects();
//    }



    @GetMapping("/checkStatusByCin/{cin}")
    public ResponseEntity<BlackListResponse> checkCustomerStatus(@PathVariable String cin) {
        return new ResponseEntity<>(clientManagementService.checkCustomerSironeStatus(cin), HttpStatus.OK);
    }
    @GetMapping("/checkStatusByRib/{rib}")
    public ResponseEntity<BlackListResponse> checkCustomerStatusByRib(@PathVariable String rib) {
        return new ResponseEntity<>(clientManagementService.addCustomerToCustomerByRib(rib), HttpStatus.OK);
    }
    @PostMapping("/addToBlackListByCin")
    public ResponseEntity<String> addToBlackListByCin(@RequestParam String cin, @RequestParam String reason) {
        clientManagementService.addToBlackListByCin(cin, reason);
        return ResponseEntity.ok("Added to black list successfully.");
    }

    @PostMapping("/addToBlackListByRib")
    public ResponseEntity<String> addToBlackListByRib(@RequestParam String rib, @RequestParam String reason) {
        clientManagementService.addToBlackListByRib(rib, reason);
        return ResponseEntity.ok("Added to black list successfully.");
    }
    @PostMapping("/addProspectiveCustomer")
    public ResponseEntity<String> addProspectiveCustomer(
            @RequestBody KYCRequest kyc) {
        try {
            clientManagementService.addProspectiveCustomer(kyc);
            return ResponseEntity.ok("Prospective customer added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/getAllTransfers")
    public ResponseEntity<List<TransferEntity>> getAllTransfers() {
        return new ResponseEntity<>(clientManagementService.getAllTransfers() ,HttpStatus.OK);
    }
    @PostMapping("/serverTransfer")
    public ResponseEntity<ServeTransferResponse> markTransferAsServedFromATM(@RequestBody TransferWithdrawRequest transferWithdrawRequest) {
        //consume transfer here

        return new ResponseEntity<>(clientTransferOperationService.markAsServed(transferWithdrawRequest) ,HttpStatus.OK);
    }
    @PutMapping("/updateKYC/{cin}")
    public ResponseEntity<UpdateKYCResponse> updateKYCInformation(
            @PathVariable String cin,
            @RequestBody KYC updatedKYC) {

      return new ResponseEntity<>(clientManagementService.updateKYCInformation(cin, updatedKYC),HttpStatus.OK);

    }
    @PostMapping("/addKYC")
    public ResponseEntity<AddKYCResponse> addKYC( @RequestBody KYCRequest kycRequest) {
        return new ResponseEntity<>(clientManagementService.addKYC( kycRequest) ,HttpStatus.OK);


    }
    @GetMapping("/check-KYC-expiration/{customerId}")
    public boolean checkKYCExpiration(@PathVariable Long customerId) {
        return clientManagementService.isKYCExpired(customerId);
    }

    @GetMapping("/getCustomer/{customerId}")
    public Optional<Customer> getCustomerById(@PathVariable Long customerId) {
        return clientManagementService.getCustomerById(customerId);


    }
    @GetMapping("getWalletById/{customerID}")
    public Wallet getWalletById(@PathVariable Long customerID) {
        return clientManagementService.getWalletById(customerID).orElse(null);
    }

    @PostMapping("/addBeneficiary")
    public ResponseEntity<AddBeneficiaryResponse> addBeneficiary(@RequestBody BeneficiaryRequest beneficiaryRequest) {

        return new ResponseEntity<>(clientManagementService.addBeneficiary(beneficiaryRequest), HttpStatus.CREATED);
    }
    @GetMapping("/findAllBeneficiariesByCustomerId/{customerId}")
    public List<Beneficiary> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        return clientManagementService.getBeneficiariesByCustomerId(customerId);
    }

    @GetMapping("findBeneficiaryById/{beneficiaryId}")
    public Beneficiary getBeneficiaryById(@PathVariable Long beneficiaryId) {
        return clientManagementService.getBeneficiaryById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with ID: " + beneficiaryId));
    }
    @PutMapping("/{beneficiaryId}/update-transfer-id")
    public ResponseEntity<Void> updateTransferIDInBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam Long transferId) {
        clientManagementService.updateTransferID(transferId, beneficiaryId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update-balance/{customerID}")
    public ResponseEntity<Void> updateWalletBalance(
            @PathVariable Long customerID,
            @RequestParam double newBalance) {
        clientManagementService.updateWalletBalance(customerID, newBalance);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update-customer-to-customer-id/{customerID}")
    public ResponseEntity<Customer> updateCustomerToCustomerID(
            @PathVariable long customerID,
            @RequestParam long customerToCustomerID) {
        Customer updatedCustomer = clientManagementService.updateCustomer_toCustomer_ID(customerID, customerToCustomerID);
        return ResponseEntity.ok(updatedCustomer);
    }
    @GetMapping("/get-by-customer-to-customer-id/{customerToCustomerID}")
    public ResponseEntity<List<Customer>> getAllCustomersByCustomerToCustomerID(
            @PathVariable Long customerToCustomerID) {
        List<Customer> customers = clientManagementService.getAllCustomersByCustomerToCustomerID(customerToCustomerID);
        return ResponseEntity.ok(customers);
    }
    @PostMapping("/check-customer-sirone-status")
    public ResponseEntity<FindCustomerByPhoneResponse> checkCustomerSironeStatus(
            @RequestBody CheckCustomerStatusByPhoneRequest checkCustomerStatusByPhoneRequest ) {
        FindCustomerByPhoneResponse response = clientManagementService.checkCustomerSironeStatusAndGetIt(checkCustomerStatusByPhoneRequest.getTel());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/")
    public String test() {
        return "hello it works ";
    }


    @PostMapping("/check-customer-sirone-status-by-rib")
    public ResponseEntity<FindCustomerByPhoneResponse> checkCustomerSironeStatusByRib(
            @RequestBody CheckCustomerStatusByRibRequest checkCustomerStatusByRibRequest ) {
        FindCustomerByPhoneResponse response = clientManagementService.checkCustomerSironeStatusAndGetItByRib(checkCustomerStatusByRibRequest.getRib());
        return ResponseEntity.ok(response);
    }
    @PutMapping("/sendOTP")
    public ResponseEntity<SendVerificationCodeResponse> sendOTP(
            @RequestBody SendVerificationCodeRequest sendVerificationCodeRequest) {

        return ResponseEntity.ok( clientManagementService.verifyIdentity(sendVerificationCodeRequest));
    }
    @PostMapping("/find-kyc")
    public FindKYCResponse findKYC(@RequestBody FindKYCRequest request) {
        return clientManagementService.findKYC( request.getIdentity());
    }
    @GetMapping("/GetAllKyc")
    public List<KYC> getAllKYC() {
        return clientManagementService.getAllKYC();
    }
    @GetMapping("/getKycByID/{id}")
    public KYC getKYCById(@PathVariable Long id) {
        Optional<KYC> kyc = clientManagementService.getKYCById(id);
        return kyc.orElse(null);
    }
    @GetMapping("/getWalletByWalletID/{id}")
    public Wallet getWalletByWalletID(@PathVariable Long id) {
   return  clientManagementService.getWalletByWalletID(id);

    }
    @GetMapping("findBeneficiaryByTransferID/{customerID}")
    public Beneficiary getBeneficiaryByTransferId(@PathVariable Long customerID) {
        return clientManagementService.getBeneficiaryByTransferId(customerID)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with ID: " + customerID));
    }
    @GetMapping("findCustomerByIdNumber/{idNumber}")
    public Customer getCustomerByIdNumber(@PathVariable String idNumber) {
        return clientManagementService.getCustomerByIdNumber(idNumber)
                ;
    }


}