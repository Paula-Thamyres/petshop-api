package com.paulathamyres.petshop.address;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(nullable = false, length = 9)
    private String cep;

    private String street;

    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    protected Address() {
    }

    public Address(String cep, String street, String neighborhood, String city, String state) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    public boolean isLocatedIn(String city, String state) {
        return this.city.equalsIgnoreCase(city) && this.state.equalsIgnoreCase(state);
    }

    public String getCep() {
        return cep;
    }

    public String getStreet() {
        return street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
