package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    public boolean existsCustomerWithId(Integer id) {
        return customerRepository.existsCustomerById(id);
    }

    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
