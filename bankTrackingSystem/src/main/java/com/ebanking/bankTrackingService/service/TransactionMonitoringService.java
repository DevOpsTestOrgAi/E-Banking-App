package com.ebanking.bankTrackingService.service;

import com.ebanking.bankTrackingService.entity.SIRONE;
import com.ebanking.bankTrackingService.entity.TrackedTransaction;
import com.ebanking.bankTrackingService.external.client.TransferServiceClient;
import com.ebanking.bankTrackingService.repository.SIRONERepository;
import com.ebanking.bankTrackingService.repository.TrackedTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TransactionMonitoringService {

    @Autowired
    private TrackedTransactionRepository trackedTransactionRepository;

    @Autowired
    private SIRONERepository sironeRepository;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @Scheduled(cron = "0 * * * * *") // Runs every minute
    public void monitorTransactions() {
        for (TrackedTransaction transaction : trackedTransactionRepository.findAll()) {
            SIRONE sirone = sironeRepository.findByCin(transaction.getCustomerIdNumber());
            if (sirone != null) {
                // Match found, block the transfer In real time
                transferServiceClient.blockTransfer(transaction.getTransferID());
            }
        }
    }
}
