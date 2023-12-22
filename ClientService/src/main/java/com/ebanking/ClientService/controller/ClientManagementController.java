package com.ebanking.ClientService.controller;


import com.ebanking.ClientService.entity.Customer;
import com.ebanking.ClientService.entity.TransferEntity;
import com.ebanking.ClientService.model.*;
import com.ebanking.ClientService.service.ClientManagementService;
import com.ebanking.ClientService.service.ClientManagementServiceImpl;
import com.ebanking.ClientService.service.ClientTransferOperationServiceImpl;
import com.ebanking.ClientService.service.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientManagementController {

    @Autowired
    private ClientManagementServiceImpl clientManagementService;
    @Autowired
    ClientTransferOperationServiceImpl clientTransferOperationService;
    @Autowired
    private  Consumer consumer;


    @GetMapping("/getAllFromTopic")
    public List<Object> consumeAndGetObjects() {

        return consumer.getAllReceivedObjects();
    }



    @GetMapping("/checkStatus/{cin}")
    public ResponseEntity<BlackListResponse> checkCustomerStatus(@PathVariable String cin) {
        return new ResponseEntity<>(clientManagementService.checkCustomerSironeStatus(cin), HttpStatus.OK);
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
    @PostMapping("/serveViaGAB")
    public ResponseEntity<ServeTransferResponse> markTransferAsServedFromATM(@RequestBody TransferWithdrawRequest transferWithdrawRequest) {
        //consume transfer here

        return new ResponseEntity<>(clientTransferOperationService.markAsServed(transferWithdrawRequest) ,HttpStatus.OK);
    }

}