package com.ebanking.TransferService.repository;

import com.ebanking.TransferService.entity.TransferEntity;
import com.ebanking.TransferService.model.TransferState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<TransferEntity,Long> {
    Optional<TransferEntity> findByReference(String ref);

    List<TransferEntity> findByCustomerId(Long idNumber);
    Long countByState(TransferState state);
}
