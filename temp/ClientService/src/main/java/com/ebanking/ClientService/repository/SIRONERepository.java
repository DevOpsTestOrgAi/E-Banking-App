package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.SIRONE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SIRONERepository extends JpaRepository<SIRONE,Long> {
    Optional<SIRONE> findByCin(String cin);
    Optional<SIRONE> findByRib(String rib);
    Optional<SIRONE> findByPhone(String rib);

}
