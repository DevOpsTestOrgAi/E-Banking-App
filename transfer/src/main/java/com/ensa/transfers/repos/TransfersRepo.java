package com.ensa.transfers.repos;

import com.ensa.transfers.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfersRepo extends JpaRepository<Transfer, String> {
}
