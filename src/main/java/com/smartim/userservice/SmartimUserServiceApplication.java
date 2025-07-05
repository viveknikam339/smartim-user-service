package com.smartim.userservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info=@Info(
				title = "User microservice REST API Documentation",
				description = "SMARTIM User microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Vivek Nikam",
						email = "viveknikam@gmail.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = " "
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "SMARTIM User microservice REST API Documentation",
				url = " "
		)
)
public class SmartimUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartimUserServiceApplication.class, args);
	}

}
