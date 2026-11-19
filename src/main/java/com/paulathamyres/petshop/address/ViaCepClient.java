package com.paulathamyres.petshop.address;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE)
public interface ViaCepClient {

    @GetExchange("/{cep}/json/")
    ViaCepResponse findByCep(@PathVariable String cep);
}
