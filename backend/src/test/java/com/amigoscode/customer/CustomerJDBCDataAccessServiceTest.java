package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(FAKER.name().fullName(), FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(), 20, Gender.MALE);
        underTest.insertCustomer(customer);

        // When
        List<Customer> actual = underTest.selectAllCustomers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, 20, Gender.MALE);
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsCustomerWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsCustomerWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        // When
        boolean actual = underTest.existsCustomerWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnsFalseWhenNotExists() {
        // Given
        int id = -1;

        // When
        boolean actual = underTest.existsCustomerWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        var newName = "foo";

        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        // When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        // When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        var newAge = 25;

        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        // When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void updateCustomerGender() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        var newGender = Gender.MALE;

        Customer update = new Customer();
        update.setId(id);
        update.setGender(newGender);

        underTest.updateCustomer(update);

        // When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(newGender);
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(), email, 20, Gender.MALE);

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        // When update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail = UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(22);
        update.setGender(Gender.MALE);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(22);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                Gender.MALE
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }
}