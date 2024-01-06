package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
   Optional<Wallet> findByCustomerId(Long customerId);

    Optional<Wallet> findByRib(String rib);

    Optional<Wallet> findByCustomer_Id(Long customerID);
}
