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

	@Bean
	public OpenAPI swaggerDocsConfig (){
		return new OpenAPI()
				.info(new Info()
						.title("Embarcadero App")
						.version("1.0")
						.description("Sistema para gestionar las embarcaciones, las guardias y los equipos de rescate en cada dique de la provincia, permite mantener un registro de las embarcaciones que acceden, el personal afectado y otros datos vitales, para estadistica y analisis. El proyecto esta en una etapa temprana de desarrollo. En esta etapa, ya se realizo el relevamiento, analisis y diseño de la solucion. Tecnologias: React Js, Java, Spring, MySQL, Swagger, Spring security, JWT, Mockito. Deploy front en vercel, back en railway y bd en clever cloud.\n" +
								"\n" +
								"Aplicacion desplegada en Vercel (Frontend), Railway (Backend) y clever cloud (Base de datos).\n" +
								"\n" +
								"Repositorio Frontend=> https://github.com/DavidCosta92/EmbarcaderoApp-Front\n" +
								"\n" +
								"Repositorio Backend=> https://github.com/DavidCosta92/EmbarcaderoApp\n" +
								"\n" +
								"Documentacion => https://embarcaderoapp-production.up.railway.app/docs/swagger-ui/index.html\n" +
								"\n" +
								"Deploy => https://embarcadero-app-front.vercel.app/")
						.summary("Deploy: https://embarcadero-app-front.vercel.app/")
						.contact(new Contact().email("davidcst2991@gmail.com").name("Costa david").url("https://www.linkedin.com/in/david-costa-yafar/")));
	}

}
