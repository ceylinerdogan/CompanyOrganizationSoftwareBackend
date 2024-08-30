package com.ceylin.companyorganizationSoftware;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "DELTA API",
				version = "1.0",
				description = "API for Company Organization Software Application"
		),
		servers = {
				@Server(
						url = "https://delta1.eu-west-1.elasticbeanstalk.com",
						description = "Generated server url"
				)
		}
)
@SpringBootApplication
public class CompanyorganizationSoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyorganizationSoftwareApplication.class, args);
	}

}
