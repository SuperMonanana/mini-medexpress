package com.example.mengyan;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MedExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedExpressApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Mini MedExpress")
						.description("A mini MedExpress application to provide consultation"));
	}

}
