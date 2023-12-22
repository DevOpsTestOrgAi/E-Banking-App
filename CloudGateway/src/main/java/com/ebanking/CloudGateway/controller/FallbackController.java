package com.ebanking.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/TransferServiceFallBack")
    public String transferServiceFallback() {
            return "Transfer Service is down!";
    }

    @GetMapping("/clientServiceFallBack")
    public String clienttServiceFallback() {
        return "Client Service is down!";
    }

    @GetMapping("/notificationServiceFallBack")
    public String notificationServiceFallBack() {
        return "Notification  Service is down!";
    }

}
