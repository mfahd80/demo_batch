package com.techshard.batch.business.customer.service;

import com.techshard.batch.business.customer.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessorExport implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        return customer;
    }
}
