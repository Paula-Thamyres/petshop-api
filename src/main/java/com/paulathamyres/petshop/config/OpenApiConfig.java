package com.paulathamyres.petshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Pet Shop API",
        version = "1.0",
        description = "Agendamento de banho, tosa e passeio com preço calculado pelo porte do pet "
                + "e taxa de leva e traz definida pelo endereço do cliente (ViaCEP)."))
public class OpenApiConfig {
}
