package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.Customer;
import io.github.devtamakuwala.dailydine.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
