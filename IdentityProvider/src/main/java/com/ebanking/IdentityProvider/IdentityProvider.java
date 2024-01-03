package com.ebanking.IdentityProvider;

import com.ebanking.IdentityProvider.auth.AuthenticationService;
import com.ebanking.IdentityProvider.auth.RegisterRequest;
import com.ebanking.IdentityProvider.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class IdentityProvider {

	public static void main(String[] args) {
		SpringApplication.run(IdentityProvider.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("admin")
					.lastname("admin")
					.email("admin@mail.com")
					.password("admin")
					.role(Role.ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("manager")
					.lastname("manager")
					.email("manager@mail.com")
					.password("manager")
					.role(Role.MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}
}
