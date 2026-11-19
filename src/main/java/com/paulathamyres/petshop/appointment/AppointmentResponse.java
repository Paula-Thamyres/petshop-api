package com.paulathamyres.petshop.appointment;

import com.paulathamyres.petshop.pricing.PetSize;
import com.paulathamyres.petshop.pricing.ServiceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long customerId,
        String customerName,
        String petName,
        PetSize petSize,
        ServiceType serviceType,
        LocalDateTime scheduledAt,
        boolean pickupAndDelivery,
        BigDecimal servicePrice,
        BigDecimal pickupFee,
        BigDecimal totalPrice) {

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getCustomer().getId(),
                appointment.getCustomer().getName(),
                appointment.getPetName(),
                appointment.getPetSize(),
                appointment.getServiceType(),
                appointment.getScheduledAt(),
                appointment.isPickupAndDelivery(),
                appointment.getServicePrice(),
                appointment.getPickupFee(),
                appointment.getTotalPrice());
    }
}
