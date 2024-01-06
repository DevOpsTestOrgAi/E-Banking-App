package com.ebanking.ClientService.repository;

import com.ebanking.ClientService.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByCin(String cin);

    Optional<Customer> findByPhone(String cin);

    Optional<Customer> findByRib(String rib);



    List<Customer> findAllByCtc(Long customerToCustomerID);
}
