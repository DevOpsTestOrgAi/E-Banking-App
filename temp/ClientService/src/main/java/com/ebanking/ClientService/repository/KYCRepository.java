package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.KYC;
import com.ebanking.ClientService.entity.SIRONE;
import com.ebanking.ClientService.model.TypeIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KYCRepository extends JpaRepository<KYC,Long> {

    Optional<KYC> findByIdNumber(String idNumber);
    boolean existsByIdNumber(String idNumber);



}
