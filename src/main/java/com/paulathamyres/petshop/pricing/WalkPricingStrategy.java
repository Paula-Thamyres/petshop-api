package com.paulathamyres.petshop.pricing;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class WalkPricingStrategy implements PricingStrategy {

    static final BigDecimal FLAT_PRICE = new BigDecimal("35.00");

    @Override
    public ServiceType serviceType() {
        return ServiceType.WALK;
    }

    @Override
    public BigDecimal calculatePrice(PetSize petSize) {
        return FLAT_PRICE;
    }
}
