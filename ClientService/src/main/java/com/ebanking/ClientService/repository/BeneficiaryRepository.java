package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
    Optional<Beneficiary> findByRib(String rib);


    List<Beneficiary> findByCustomerId(Long customerId);



    Optional<Beneficiary> findByCinAndTransferID(String cin, Long transferID);

    Optional<Beneficiary> findByTransferID(Long beneficiaryId);
}
