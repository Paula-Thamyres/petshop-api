package com.paulathamyres.petshop.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("petshop")
public record PetshopProperties(
        @NotBlank String city,
        @NotBlank String state,
        @NotNull @PositiveOrZero BigDecimal pickupFee) {
}
