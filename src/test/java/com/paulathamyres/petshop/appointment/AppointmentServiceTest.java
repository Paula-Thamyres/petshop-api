package com.paulathamyres.petshop.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paulathamyres.petshop.address.Address;
import com.paulathamyres.petshop.config.PetshopProperties;
import com.paulathamyres.petshop.customer.Customer;
import com.paulathamyres.petshop.customer.CustomerRepository;
import com.paulathamyres.petshop.pricing.BathPricingStrategy;
import com.paulathamyres.petshop.pricing.GroomingPricingStrategy;
import com.paulathamyres.petshop.pricing.PetSize;
import com.paulathamyres.petshop.pricing.PriceCalculator;
import com.paulathamyres.petshop.pricing.ServiceType;
import com.paulathamyres.petshop.pricing.WalkPricingStrategy;
import com.paulathamyres.petshop.shared.exception.BusinessRuleException;
import com.paulathamyres.petshop.shared.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    private static final long CUSTOMER_ID = 1L;
    private static final LocalDateTime NEXT_WEEK = LocalDateTime.now().plusWeeks(1);

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    private AppointmentService service;

    @BeforeEach
    void setUp() {
        PriceCalculator priceCalculator = new PriceCalculator(List.of(
                new BathPricingStrategy(), new GroomingPricingStrategy(), new WalkPricingStrategy()));
        PickupFeeCalculator pickupFeeCalculator = new PickupFeeCalculator(
                new PetshopProperties("Araras", "SP", new BigDecimal("15.00")));

        service = new AppointmentService(appointmentRepository, customerRepository, priceCalculator, pickupFeeCalculator);
    }

    @Test
    @DisplayName("Agenda somando o preço do serviço e a taxa de leva e traz")
    void schedulesWithServicePriceAndPickupFee() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerIn("Araras")));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentResponse response = service.schedule(bathForMediumPet(true));

        assertThat(response.petName()).isEqualTo("Thor");
        assertThat(response.servicePrice()).isEqualTo(new BigDecimal("70.00"));
        assertThat(response.pickupFee()).isEqualTo(new BigDecimal("15.00"));
        assertThat(response.totalPrice()).isEqualTo(new BigDecimal("85.00"));
    }

    @Test
    @DisplayName("Recusa agendamento para cliente que não existe")
    void rejectsUnknownCustomer() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.schedule(bathForMediumPet(false)))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não salva o agendamento quando o leva e traz não atende a cidade")
    void doesNotSaveWhenPickupIsOutsideServiceArea() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerIn("São Paulo")));

        assertThatThrownBy(() -> service.schedule(bathForMediumPet(true)))
                .isInstanceOf(BusinessRuleException.class);
        verify(appointmentRepository, never()).save(any());
    }

    private static CreateAppointmentRequest bathForMediumPet(boolean pickupAndDelivery) {
        return new CreateAppointmentRequest(
                CUSTOMER_ID, " Thor ", PetSize.MEDIUM, ServiceType.BATH, NEXT_WEEK, pickupAndDelivery);
    }

    private static Customer customerIn(String city) {
        return new Customer("Maria Souza", "19999990000", new Address("00000-000", "Rua Teste", "Centro", city, "SP"));
    }
}
