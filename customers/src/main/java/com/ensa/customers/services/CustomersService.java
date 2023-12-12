package com.ensa.customers.services;

import com.ensa.customers.dtos.CustomerDto;
import com.ensa.customers.repos.CustomersRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomersService {
    private final CustomersRepo customersRepo;

    public List<CustomerDto> getCustomers() {
        return  List.of(
                new CustomerDto("X", "Y", "Z", "S"),
                new CustomerDto("R", "F", "K", "M")
        );
    }
}
