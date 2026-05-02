package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CreditCardId(UUID id) {

    public CreditCardId() {
            this(IdGenerator.generateTimeBasedUUID());
    }
    public CreditCardId {
        Objects.requireNonNull(id);
    }

}
