package com.paulathamyres.petshop.pricing;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    private final Map<ServiceType, PricingStrategy> strategies;

    public PriceCalculator(List<PricingStrategy> strategies) {
        this.strategies = indexByServiceType(strategies);
    }

    public BigDecimal calculate(ServiceType serviceType, PetSize petSize) {
        return strategies.get(serviceType).calculatePrice(petSize);
    }

    private static Map<ServiceType, PricingStrategy> indexByServiceType(List<PricingStrategy> strategies) {
        Map<ServiceType, PricingStrategy> index = new EnumMap<>(ServiceType.class);
        for (PricingStrategy strategy : strategies) {
            PricingStrategy previous = index.put(strategy.serviceType(), strategy);
            if (previous != null) {
                throw new IllegalStateException("More than one pricing strategy for " + strategy.serviceType());
            }
        }
        Set<ServiceType> missing = EnumSet.allOf(ServiceType.class);
        missing.removeAll(index.keySet());
        if (!missing.isEmpty()) {
            throw new IllegalStateException("No pricing strategy for " + missing);
        }
        return Collections.unmodifiableMap(index);
    }
}
