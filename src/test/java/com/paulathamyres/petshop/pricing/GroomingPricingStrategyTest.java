package com.paulathamyres.petshop.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GroomingPricingStrategyTest {

    private final GroomingPricingStrategy strategy = new GroomingPricingStrategy();

    @ParameterizedTest(name = "porte {0} custa {1}")
    @CsvSource({"SMALL, 80.00", "MEDIUM, 100.00", "LARGE, 120.00"})
    @DisplayName("Tosa aplica acréscimo sobre o preço base conforme o porte")
    void appliesSurchargePerSize(PetSize petSize, BigDecimal expectedPrice) {
        assertThat(strategy.calculatePrice(petSize)).isEqualTo(expectedPrice);
    }
}
