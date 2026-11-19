package com.paulathamyres.petshop.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(String cep, String logradouro, String bairro, String localidade, String uf) {

    boolean isNotFound() {
        return localidade == null || uf == null;
    }

    Address toAddress() {
        return new Address(cep, logradouro, bairro, localidade, uf);
    }
}
