package com.ebanking.TransferService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CLIENT-SERVICE", url = "http://localhost:8087/api/client")
public interface ExternalClientService {
    @PostMapping("/addToBlackListByCin")
    public ResponseEntity<String> addToBlackListByCin(@RequestParam String cin, @RequestParam String reason);


    @PostMapping("/addToBlackListByRib")
    public ResponseEntity<String> addToBlackListByRib(@RequestParam String rib, @RequestParam String reason);

}
