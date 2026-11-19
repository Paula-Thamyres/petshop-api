package com.paulathamyres.petshop.customer;

import com.paulathamyres.petshop.address.Address;
import com.paulathamyres.petshop.address.AddressLookupService;
import com.paulathamyres.petshop.shared.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressLookupService addressLookupService;

    public CustomerService(CustomerRepository customerRepository, AddressLookupService addressLookupService) {
        this.customerRepository = customerRepository;
        this.addressLookupService = addressLookupService;
    }

    public CustomerResponse register(CreateCustomerRequest request) {
        Address address = addressLookupService.findByCep(request.cep());
        Customer customer = new Customer(request.name().strip(), request.phone(), address);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }
}
