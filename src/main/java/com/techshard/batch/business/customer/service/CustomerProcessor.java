package com.techshard.batch.business.customer.service;

import com.techshard.batch.business.customer.entity.Customer;
import com.techshard.batch.business.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@AllArgsConstructor
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    private final CustomerRepository customerRepository;

    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getGender() == null){

        }
        return customer;
    }
}
