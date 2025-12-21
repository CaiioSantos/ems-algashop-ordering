package com.algaworks.algashop.ordering.domain.exceptions;

public class DomainException extends RuntimeException {

    public DomainException(Throwable cause) {
        super(cause);
    }

    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
