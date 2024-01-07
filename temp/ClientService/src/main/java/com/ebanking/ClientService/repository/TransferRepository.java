package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity,Long> {


}
