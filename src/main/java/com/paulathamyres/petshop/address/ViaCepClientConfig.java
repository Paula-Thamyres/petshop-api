package com.paulathamyres.petshop.address;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "viacep", types = ViaCepClient.class)
public class ViaCepClientConfig {
}
