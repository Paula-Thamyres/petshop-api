package com.paulathamyres.petshop.customer;

import com.paulathamyres.petshop.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 11)
    private String phone;

    @Embedded
    private Address address;

    protected Customer() {
    }

    public Customer(String name, String phone, Address address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }
}
