package com.algaworks.algashop.ordering.domain.customer;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;

public class CustomerEmailsInUseException extends DomainException {
    public CustomerEmailsInUseException(CustomerId customerId) {
        super(ErrorMessages.ERROR_CUSTOMER_EMAIL_IS_IN_USE);
    }
}
