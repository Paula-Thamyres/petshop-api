package com.paulathamyres.petshop.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class GroomingPricingStrategy implements PricingStrategy {

    static final BigDecimal BASE_PRICE = new BigDecimal("80.00");
    static final BigDecimal MEDIUM_PET_SURCHARGE = new BigDecimal("0.25");
    static final BigDecimal LARGE_PET_SURCHARGE = new BigDecimal("0.50");

    private static final int MONEY_SCALE = 2;

    @Override
    public ServiceType serviceType() {
        return ServiceType.GROOMING;
    }

    @Override
    public BigDecimal calculatePrice(PetSize petSize) {
        BigDecimal surcharge = BASE_PRICE.multiply(surchargeRate(petSize));
        return BASE_PRICE.add(surcharge).setScale(MONEY_SCALE, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal surchargeRate(PetSize petSize) {
        return switch (petSize) {
            case SMALL -> BigDecimal.ZERO;
            case MEDIUM -> MEDIUM_PET_SURCHARGE;
            case LARGE -> LARGE_PET_SURCHARGE;
        };
    }
}
