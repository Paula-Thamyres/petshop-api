package com.paulathamyres.petshop.pricing;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class BathPricingStrategy implements PricingStrategy {

    static final BigDecimal SMALL_PET_PRICE = new BigDecimal("50.00");
    static final BigDecimal MEDIUM_PET_PRICE = new BigDecimal("70.00");
    static final BigDecimal LARGE_PET_PRICE = new BigDecimal("90.00");

    @Override
    public ServiceType serviceType() {
        return ServiceType.BATH;
    }

    @Override
    public BigDecimal calculatePrice(PetSize petSize) {
        return switch (petSize) {
            case SMALL -> SMALL_PET_PRICE;
            case MEDIUM -> MEDIUM_PET_PRICE;
            case LARGE -> LARGE_PET_PRICE;
        };
    }
}
