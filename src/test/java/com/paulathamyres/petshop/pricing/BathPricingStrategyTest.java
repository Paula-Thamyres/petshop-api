package com.paulathamyres.petshop.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BathPricingStrategyTest {

    private final BathPricingStrategy strategy = new BathPricingStrategy();

    @ParameterizedTest(name = "porte {0} custa {1}")
    @CsvSource({"SMALL, 50.00", "MEDIUM, 70.00", "LARGE, 90.00"})
    @DisplayName("Banho cobra um valor fixo para cada porte")
    void chargesFixedPricePerSize(PetSize petSize, BigDecimal expectedPrice) {
        assertThat(strategy.calculatePrice(petSize)).isEqualTo(expectedPrice);
    }
}
