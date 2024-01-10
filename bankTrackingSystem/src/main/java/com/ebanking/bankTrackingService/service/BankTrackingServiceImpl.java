package com.ebanking.bankTrackingService.service;

import com.ebanking.bankTrackingService.entity.TrackedTransaction;
import com.ebanking.bankTrackingService.repository.TrackedTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankTrackingServiceImpl implements BankTrackingService {

@Autowired
private TrackedTransactionRepository trackedTransactionRepository;

    @Override
    public void addTrackedTransaction(Long transferID,String idNumber) {
        TrackedTransaction trackedTransaction=TrackedTransaction
                .builder()
                .customerIdNumber(idNumber)
                .transferID(transferID)
                .build();
        trackedTransactionRepository.save(trackedTransaction);

    }
}
