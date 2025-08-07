package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    // Compact constructor com validação
    public Money {
        Objects.requireNonNull(value, "Valor não pode ser nulo");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo");
        }

        value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money(String valueString) {
        this(new BigDecimal(valueString));
    }

    public Money add(Money money) {
        Objects.requireNonNull(money);
        return new Money(this.value.add(money.value));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa");
        }
        return new Money(this.value.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public Money divide(Money divisor) {
        return new Money(this.value.divide(divisor.value, RoundingMode.HALF_EVEN));
    }

    @Override
    public String toString() {
        return "R$ " + value.toPlainString();
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }
}