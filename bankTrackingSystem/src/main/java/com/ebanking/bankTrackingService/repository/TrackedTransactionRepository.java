package com.ebanking.bankTrackingService.repository;

import com.ebanking.bankTrackingService.entity.TrackedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackedTransactionRepository extends JpaRepository<TrackedTransaction, Long> {


}
