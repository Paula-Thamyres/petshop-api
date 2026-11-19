package com.paulathamyres.petshop.pricing;

import java.math.BigDecimal;

public interface PricingStrategy {

    ServiceType serviceType();

    BigDecimal calculatePrice(PetSize petSize);
}
