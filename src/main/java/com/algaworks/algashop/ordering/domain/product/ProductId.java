package com.algaworks.algashop.ordering.domain.product;

import com.algaworks.algashop.ordering.domain.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId(){
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ProductId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
