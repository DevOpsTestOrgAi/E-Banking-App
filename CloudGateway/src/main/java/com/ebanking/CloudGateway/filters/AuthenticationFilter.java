package com.ebanking.CloudGateway.filters;

import com.ebanking.CloudGateway.feignClients.FeignAuthInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RestTemplate restTemplate;
    //private FeignAuthInterface feignAuthInterface;

//    @Autowired
//    public void setFeignAuthInterface(FeignAuthInterface feignAuthInterface) {
//        this.feignAuthInterface = feignAuthInterface;
//    }

    public AuthenticationFilter(RestTemplate restTemplate) {
        super(Config.class);
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("Passed from AuthenticationFilter apply method");
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
                // REST call to AUTH service using RestTemplate
                restTemplate.getForObject("http://localhost:8080/api/v1/auth/validate-token?token=" + authHeader, Void.class);
                //feignAuthInterface.validateToken(authHeader);
                System.out.println("Auth token: " + authHeader);
            } catch (Exception e) {
                System.out.println("invalid access...!");
                System.out.println(e.getMessage());
                throw new RuntimeException("un authorized access to application");
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

    @Configuration
    public static class RestTemplateConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
