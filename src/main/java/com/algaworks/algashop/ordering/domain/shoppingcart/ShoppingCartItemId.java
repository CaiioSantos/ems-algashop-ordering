package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartItemId(UUID value) {

    public ShoppingCartItemId(){
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ShoppingCartItemId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
