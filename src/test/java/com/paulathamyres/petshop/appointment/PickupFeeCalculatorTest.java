package com.paulathamyres.petshop.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.paulathamyres.petshop.address.Address;
import com.paulathamyres.petshop.config.PetshopProperties;
import com.paulathamyres.petshop.shared.exception.BusinessRuleException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PickupFeeCalculatorTest {

    private static final BigDecimal PICKUP_FEE = new BigDecimal("15.00");

    private final PickupFeeCalculator calculator =
            new PickupFeeCalculator(new PetshopProperties("Araras", "SP", PICKUP_FEE));

    @Test
    @DisplayName("Não cobra taxa quando o cliente não pede leva e traz")
    void noFeeWithoutPickup() {
        assertThat(calculator.calculate(addressIn("São Paulo", "SP"), false)).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("Cobra a taxa quando o cliente mora na cidade do pet shop")
    void chargesFeeInsideServiceArea() {
        assertThat(calculator.calculate(addressIn("Araras", "SP"), true)).isEqualTo(PICKUP_FEE);
    }

    @Test
    @DisplayName("Compara cidade e estado sem diferenciar maiúsculas")
    void comparesCityIgnoringCase() {
        assertThat(calculator.calculate(addressIn("ARARAS", "sp"), true)).isEqualTo(PICKUP_FEE);
    }

    @Test
    @DisplayName("Recusa leva e traz para outra cidade")
    void rejectsPickupOutsideServiceArea() {
        assertThatThrownBy(() -> calculator.calculate(addressIn("São Paulo", "SP"), true))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("O leva e traz atende apenas clientes de Araras/SP.");
    }

    private static Address addressIn(String city, String state) {
        return new Address("00000-000", "Rua Teste", "Centro", city, state);
    }
}
