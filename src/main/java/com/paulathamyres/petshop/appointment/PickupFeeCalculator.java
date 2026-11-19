package com.paulathamyres.petshop.appointment;

import com.paulathamyres.petshop.address.Address;
import com.paulathamyres.petshop.config.PetshopProperties;
import com.paulathamyres.petshop.shared.exception.BusinessRuleException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class PickupFeeCalculator {

    static final BigDecimal NO_FEE = new BigDecimal("0.00");

    private final PetshopProperties properties;

    public PickupFeeCalculator(PetshopProperties properties) {
        this.properties = properties;
    }

    public BigDecimal calculate(Address customerAddress, boolean pickupRequested) {
        if (!pickupRequested) {
            return NO_FEE;
        }
        if (!customerAddress.isLocatedIn(properties.city(), properties.state())) {
            throw new BusinessRuleException("O leva e traz atende apenas clientes de %s/%s."
                    .formatted(properties.city(), properties.state()));
        }
        return properties.pickupFee();
    }
}
