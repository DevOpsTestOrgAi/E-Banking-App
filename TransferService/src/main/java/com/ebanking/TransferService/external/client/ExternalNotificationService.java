package com.ebanking.TransferService.external.client;

import com.ebanking.TransferService.model.SMS;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NOTIFICATIONS-SERVICE", url = "http://192.168.0.52:8088/notifications")
public interface ExternalNotificationService {
    @PostMapping("/send-sms")
     String sendSMS(@RequestBody SMS sms);

    @PostMapping("/OTP")
     String verifyIdentity(@RequestParam String phone, @RequestParam String code) ;
}
