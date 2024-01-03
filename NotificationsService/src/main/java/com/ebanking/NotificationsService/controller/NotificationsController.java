package com.ebanking.NotificationsService.controller;

import com.ebanking.NotificationsService.model.Customer;
import com.ebanking.NotificationsService.model.SMS;
import com.ebanking.NotificationsService.service.NotificationsServcie;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationsController {
    @Autowired
    NotificationsServcie notificationsServcie  ;
    @PostMapping("/send-sms")
    public String sendSMS(@RequestBody SMS sms) {
        return notificationsServcie.sendSMS(sms);
    }

    @PostMapping("/OTP")
    public String verifyIdentity(@RequestParam String phone, @RequestParam String code) {
        return notificationsServcie.verifyIdentity(phone, code);
    }
    @PostMapping("/test")

    public String test(@RequestBody Customer customer
    ) throws JsonProcessingException {

        return notificationsServcie.test(customer);
    }

    // Todo: Remove this method later
    @GetMapping("/testMe")
    public List<String> getNotificationsTestAuth() throws JsonProcessingException {
        return List.of("It Works!");
    }
}
