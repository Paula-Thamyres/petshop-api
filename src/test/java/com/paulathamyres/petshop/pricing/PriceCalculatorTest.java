package com.paulathamyres.petshop.pricing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator(List.of(
            new BathPricingStrategy(),
            new GroomingPricingStrategy(),
            new WalkPricingStrategy()));

    @Test
    @DisplayName("Usa a estratégia do serviço escolhido")
    void delegatesToStrategyOfChosenService() {
        assertThat(calculator.calculate(ServiceType.BATH, PetSize.MEDIUM)).isEqualTo(new BigDecimal("70.00"));
        assertThat(calculator.calculate(ServiceType.GROOMING, PetSize.LARGE)).isEqualTo(new BigDecimal("120.00"));
        assertThat(calculator.calculate(ServiceType.WALK, PetSize.SMALL)).isEqualTo(new BigDecimal("35.00"));
    }

    @Test
    @DisplayName("Falha na inicialização quando falta estratégia para algum serviço")
    void failsWhenServiceHasNoStrategy() {
        List<PricingStrategy> onlyBath = List.of(new BathPricingStrategy());

        assertThatIllegalStateException()
                .isThrownBy(() -> new PriceCalculator(onlyBath))
                .withMessageContaining("GROOMING")
                .withMessageContaining("WALK");
    }

    @Test
    @DisplayName("Falha na inicialização quando um serviço tem duas estratégias")
    void failsWhenServiceHasTwoStrategies() {
        List<PricingStrategy> duplicated = List.of(
                new BathPricingStrategy(),
                new BathPricingStrategy(),
                new GroomingPricingStrategy(),
                new WalkPricingStrategy());

        assertThatIllegalStateException()
                .isThrownBy(() -> new PriceCalculator(duplicated))
                .withMessageContaining("BATH");
    }
}
