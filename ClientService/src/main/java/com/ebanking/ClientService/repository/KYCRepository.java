package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KYCRepository extends JpaRepository<KYC,Long> {

    KYC findByIdNumber(String idNumber);
    boolean existsByIdNumber(String idNumber);
}
