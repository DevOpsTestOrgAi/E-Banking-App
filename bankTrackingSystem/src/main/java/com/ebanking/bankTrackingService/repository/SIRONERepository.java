package com.ebanking.bankTrackingService.repository;

import com.ebanking.bankTrackingService.entity.SIRONE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SIRONERepository extends JpaRepository<SIRONE,Long> {
    SIRONE findByCin(String cin);
}
