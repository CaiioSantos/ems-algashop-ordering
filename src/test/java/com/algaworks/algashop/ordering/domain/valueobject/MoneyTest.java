package com.algaworks.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithBigDecimal() {
        Money money = new Money(new BigDecimal("10"));
        Assertions.assertThat(money.value()).isEqualByComparingTo("10.00");

    }

    @Test
    void shouldCreateMoneyWithString() {
        Money money = new Money("25.456");
        Assertions.assertThat(money.value()).isEqualByComparingTo("25.46");

    }

    @Test
    void shouldRejectNegativeValue() {
        Throwable thrown = Assertions.catchThrowable(() -> new Money(new BigDecimal("-1")));

        Assertions.assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(thrown.getMessage())
                .contains("O valor não pode ser negativo");
    }


    @Test
    void shouldCreateZeroConstantCorrectly() {
        Assertions.assertThat(Money.ZERO.value()).isEqualByComparingTo("0.00");
    }
}