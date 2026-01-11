package com.algaworks.algashop.ordering.domain.customer;

import com.algaworks.algashop.ordering.domain.DomainException;

import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }

    public CustomerArchivedException(Throwable cause) {
        super(ERROR_CUSTOMER_ARCHIVED, cause);
    }
}
