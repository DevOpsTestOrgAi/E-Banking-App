package com.ebanking.IdentityProvider.auth;

import com.ebanking.IdentityProvider.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ManagingClientsController {
    @PostMapping("/agents")
    public ResponseEntity<User> createAgent(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/wallets")
    public ResponseEntity<AuthenticationResponse> createWallet(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(null);
    }

}
