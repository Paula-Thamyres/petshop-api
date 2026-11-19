package com.paulathamyres.petshop.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class WalkPricingStrategyTest {

    private final WalkPricingStrategy strategy = new WalkPricingStrategy();

    @ParameterizedTest(name = "porte {0}")
    @EnumSource(PetSize.class)
    @DisplayName("Passeio tem o mesmo preço para qualquer porte")
    void chargesSamePriceForAnySize(PetSize petSize) {
        assertThat(strategy.calculatePrice(petSize)).isEqualTo(new BigDecimal("35.00"));
    }
}
