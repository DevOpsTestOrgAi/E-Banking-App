package com.ebanking.CloudGateway.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@FeignClient("IDENTITY-PROVIDER/api/v1/auth")
public interface FeignAuthInterface {
    @GetMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam String token) throws IllegalAccessException;
}
