package com.Embarcadero.demo;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmbarcaderoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbarcaderoAppApplication.class, args);
	}

}
