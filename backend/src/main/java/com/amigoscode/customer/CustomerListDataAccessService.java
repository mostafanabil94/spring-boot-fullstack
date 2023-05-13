package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    private static final ArrayList<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 21);
        customers.add(alex);

        Customer jamila = new Customer(2, "Jamila", "jamila@gmail.com", 19);
        customers.add(jamila);
    }

    public List<Customer> selectAllCustomers() {
        return customers;
    }

    public Optional<Customer> selectCustomerById(Integer id) {
        return selectAllCustomers().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    public boolean existsCustomerWithEmail(String email) {
        return selectAllCustomers().stream().anyMatch(c -> c.getEmail().equals(email));
    }

    public boolean existsCustomerWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    public void updateCustomer(Customer customer) {
        customers.stream()
                .filter(c -> c.getId().equals(customer.getId()))
                .findFirst()
                .ifPresent(c -> customers.set(customers.indexOf(c), customer));
    }
}
