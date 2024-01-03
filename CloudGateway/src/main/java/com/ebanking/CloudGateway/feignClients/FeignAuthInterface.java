package com.ebanking.CloudGateway.feignClients;

import com.ebanking.IdentityProvider.auth.AuthenticationRequest;
import com.ebanking.IdentityProvider.auth.AuthenticationResponse;
import com.ebanking.IdentityProvider.auth.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@FeignClient("IDENTITY-PROVIDER/api/v1/auth")
public interface FeignAuthInterface {
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    );
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    );

    @GetMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam String token) ;

//    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    );
}
