package com.ensa.customers.controllers;

import com.ensa.customers.dtos.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/customers")
@AllArgsConstructor
public class CustomersController {
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getPosts() {
        return  List.of("Hello");
    }

}
