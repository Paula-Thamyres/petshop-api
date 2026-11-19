package com.paulathamyres.petshop.customer;

import com.paulathamyres.petshop.address.AddressResponse;

public record CustomerResponse(Long id, String name, String phone, AddressResponse address) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                AddressResponse.from(customer.getAddress()));
    }
}
