package com.paulathamyres.petshop.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(

        @Schema(example = "Maria Souza")
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,

        @Schema(example = "19999990000")
        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter DDD e só números (10 ou 11 dígitos).")
        String phone,

        @Schema(example = "13600-061")
        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O CEP deve ter 8 dígitos, com ou sem hífen.")
        String cep) {
}
