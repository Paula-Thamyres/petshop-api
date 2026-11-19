package com.paulathamyres.petshop.appointment;

import com.paulathamyres.petshop.customer.Customer;
import com.paulathamyres.petshop.customer.CustomerRepository;
import com.paulathamyres.petshop.pricing.PriceCalculator;
import com.paulathamyres.petshop.shared.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final PriceCalculator priceCalculator;
    private final PickupFeeCalculator pickupFeeCalculator;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              CustomerRepository customerRepository,
                              PriceCalculator priceCalculator,
                              PickupFeeCalculator pickupFeeCalculator) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.priceCalculator = priceCalculator;
        this.pickupFeeCalculator = pickupFeeCalculator;
    }

    @Transactional
    public AppointmentResponse schedule(CreateAppointmentRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + request.customerId()));

        BigDecimal servicePrice = priceCalculator.calculate(request.serviceType(), request.petSize());
        BigDecimal pickupFee = pickupFeeCalculator.calculate(customer.getAddress(), request.pickupAndDelivery());

        Appointment appointment = new Appointment(
                customer,
                request.petName().strip(),
                request.petSize(),
                request.serviceType(),
                request.scheduledAt(),
                request.pickupAndDelivery(),
                servicePrice,
                pickupFee);

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + id));
    }
}
