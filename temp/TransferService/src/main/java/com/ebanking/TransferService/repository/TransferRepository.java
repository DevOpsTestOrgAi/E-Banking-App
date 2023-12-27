package com.ebanking.TransferService.repository;

import com.ebanking.TransferService.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferEntity,Long> {
    Optional<TransferEntity> findByReference(String ref);

}
