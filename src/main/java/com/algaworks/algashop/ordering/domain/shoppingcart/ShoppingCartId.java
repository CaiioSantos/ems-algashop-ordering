package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId(){
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ShoppingCartId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
