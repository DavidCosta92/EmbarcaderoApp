package com.Embarcadero.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
                contact = @Contact(
                        name = "Costa david",
                        email = "davidcst2991@gmail.com",
                        url = "https://www.linkedin.com/in/david-costa-yafar/"
                ),
                title = "Embarcadero App",
                version = "1.0",
                description = "Sistema para gestionar las embarcaciones, las guardias y los equipos de rescate en cada dique de la provincia, permite mantener un registro de las embarcaciones que acceden, el personal afectado y otros datos vitales, para estadistica y analisis. El proyecto esta en una etapa temprana de desarrollo. En esta etapa, ya se realizo el relevamiento, analisis y diseño de la solucion. Tecnologias: React Js, Java, Spring, MySQL, Swagger, Spring security, JWT, Mockito. Deploy front en vercel, back en railway y bd en clever cloud.\n" +
                        "\n" +
                        "Aplicacion desplegada en Vercel (Frontend), Railway (Backend) y clever cloud (Base de datos).\n" +
                        "\n" +
                        "Repositorio Frontend=> https://github.com/DavidCosta92/EmbarcaderoApp-Front\n" +
                        "\n" +
                        "Repositorio Backend=> https://github.com/DavidCosta92/EmbarcaderoApp\n" +
                        "\n" +
                        "Documentacion => https://embarcaderoapp-production.up.railway.app/docs/swagger-ui/index.html\n" +
                        "\n" +
                        "Deploy => https://embarcadero-app-front.vercel.app/\n" +
                        "\n" +
                        "NOTA: Para probar los endpoints deje creado una clave especial, debes hacer click en el boton 'Authorize' y en el campo value colocar 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYXZpZENvc3RhIiwiaWF0IjoxNzEzNTMzNzY4LCJleHAiOjE3MTM2MjAxNjh9.8Hmj-z-cIU0GTOELnso1VAJwmKvL7aWj8pTgEEq7cRo', esto permitira ingresar a todos los endpoints con rol 'ADMIN' ",
                license = @License(
                        name = "",
                        url = ""
                ),
                termsOfService = ""
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "Add your token below",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}

