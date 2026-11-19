package com.paulathamyres.petshop.appointment;

import com.paulathamyres.petshop.customer.Customer;
import com.paulathamyres.petshop.pricing.PetSize;
import com.paulathamyres.petshop.pricing.ServiceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 50)
    private String petName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PetSize petSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ServiceType serviceType;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private boolean pickupAndDelivery;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal servicePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pickupFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    protected Appointment() {
    }

    public Appointment(Customer customer, String petName, PetSize petSize, ServiceType serviceType,
                       LocalDateTime scheduledAt, boolean pickupAndDelivery,
                       BigDecimal servicePrice, BigDecimal pickupFee) {
        this.customer = customer;
        this.petName = petName;
        this.petSize = petSize;
        this.serviceType = serviceType;
        this.scheduledAt = scheduledAt;
        this.pickupAndDelivery = pickupAndDelivery;
        this.servicePrice = servicePrice;
        this.pickupFee = pickupFee;
        this.totalPrice = servicePrice.add(pickupFee);
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getPetName() {
        return petName;
    }

    public PetSize getPetSize() {
        return petSize;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public boolean isPickupAndDelivery() {
        return pickupAndDelivery;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public BigDecimal getPickupFee() {
        return pickupFee;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
