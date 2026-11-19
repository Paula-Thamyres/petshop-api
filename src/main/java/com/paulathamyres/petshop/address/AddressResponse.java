package com.paulathamyres.petshop.address;

public record AddressResponse(String cep, String street, String neighborhood, String city, String state) {

    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getCep(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState());
    }
}
