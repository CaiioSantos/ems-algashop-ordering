package com.algaworks.algashop.ordering.domain.customer;

import com.algaworks.algashop.ordering.domain.DomainEntityNotFoundException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;

public class CustomerNotFoundException extends DomainEntityNotFoundException {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND,customerId));
    }
}
