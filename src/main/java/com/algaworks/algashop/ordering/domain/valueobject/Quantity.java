package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value, "Valor não pode ser nulo");
        if (value < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo");
        }
    }

    public Quantity add(Quantity quantity) {
        Objects.requireNonNull(quantity);
        return new Quantity(this.value + quantity.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Quantity o) {
        return this.value().compareTo(o.value());
    }
}
