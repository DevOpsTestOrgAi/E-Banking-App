package com.ebanking.ClientService.external.client;

import com.ebanking.ClientService.model.SMS;
import com.ebanking.ClientService.model.SendVerificationCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NOTIFICATIONS-SERVICE", url = "http://localhost:8088/notifications")
public interface ExternalNotificationService {
    @PostMapping("/send-sms")
    String sendSMS(@RequestBody SMS sms);

    @PostMapping("/OTP")
     ResponseEntity<SendVerificationCodeResponse> verifyIdentity(@RequestParam String phone, @RequestParam String code) ;


}