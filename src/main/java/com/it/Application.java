package com.it;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

// swagger info
@OpenAPIDefinition(
		info = @Info(
				title = "Custom-database-User-authentication-and-authorization-using-JWT-API",
				version = "1.0",
				description = "In this Api we used Spring security, Validation and Jwt implementation for authentication and authorization and we solved all types of exception in running test cases.",
				contact = @Contact(
						name = "Kundan Kumar Chourasiya",
						email = "mailmekundanchourasiya@gmail.com"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Custom-database-User-authentication-and-authorization-using-JWT-url"
		)
)

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
