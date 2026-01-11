package com.algaworks.algashop.ordering.domain.customer;

import com.algaworks.algashop.ordering.domain.Repository;
import com.algaworks.algashop.ordering.domain.commons.Email;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
    Optional<Customer> ofEmail(Email email);
    boolean isEmailUnique(Email email, CustomerId exceptCustomerId);
}
