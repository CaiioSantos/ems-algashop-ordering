package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

class QuantityTest {

    @Test
    void shouldCreateValidQuantity() {
        Quantity quantity = new Quantity(5);
        Assertions.assertThat(quantity.value()).isEqualTo(5);
    }

    @Test
    void shouldRejectNullValue() {
        Assertions.assertThatThrownBy(() -> new Quantity(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Valor não pode ser nulo");
    }

    @Test
    void shouldRejectNegativeValue() {
        Assertions.assertThatThrownBy(() -> new Quantity(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("O valor não pode ser negativo");
    }

    @Test
    void shouldAddQuantitiesCorrectly() {
        Quantity q1 = new Quantity(2);
        Quantity q2 = new Quantity(3);
        Quantity result = q1.add(q2);

        Assertions.assertThat(result.value()).isEqualTo(5);
    }

    @Test
    void shouldCompareQuantitiesCorrectly() {
        Quantity smaller = new Quantity(1);
        Quantity bigger = new Quantity(10);

        Assertions.assertThat(smaller.compareTo(bigger)).isLessThan(0);
        Assertions.assertThat(bigger.compareTo(smaller)).isGreaterThan(0);
        Assertions.assertThat(smaller.compareTo(new Quantity(1))).isZero();
    }
}