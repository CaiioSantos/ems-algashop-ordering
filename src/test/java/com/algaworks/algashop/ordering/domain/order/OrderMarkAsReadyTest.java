package com.algaworks.algashop.ordering.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderMarkAsReadyTest {

    @Test
    public void givenPaidOrder_whenMarkAsReady_shouldUpdateStatusAndTimestamp(){
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        order.markAsPaid();
        order.markAsReady();
        Assertions.assertWith(order.status()).isEqualTo(OrderStatus.READY);
        Assertions.assertWith(order.readyAt()).isNotNull();
    }
    @Test
    public void givenDraftOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState(){
        Order order = OrderTestDataBuilder.anOrder().build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.markAsReady());

        Assertions.assertWith(order.status()).isEqualTo(OrderStatus.DRAFT);
        Assertions.assertWith(order.readyAt()).isNull();
    }

    @Test
    void givenReadyOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        order.markAsPaid();
        order.markAsReady();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.markAsReady());

        Assertions.assertWith(order.status()).isEqualTo(OrderStatus.READY);
        Assertions.assertWith(order.readyAt()).isNotNull();
    }

    @Test
    void givenPlacedOrder_whenMarkAsReady_shouldThrowExceptionAndNotChangeState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.markAsReady());

        Assertions.assertWith(order.status()).isEqualTo(OrderStatus.PLACED);
        Assertions.assertWith(order.readyAt()).isNull();
    }
}

