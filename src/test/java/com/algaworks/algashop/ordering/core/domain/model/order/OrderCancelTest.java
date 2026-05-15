package com.algaworks.algashop.ordering.core.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderCancelTest {

    @Test
    void givenEmptyOrder_whenCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();
        Assertions.assertThat(order.canceledAt()).isNotNull();
        Assertions.assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
        Assertions.assertThat(order.isCanceled()).isTrue();
    }


    @Test
    void givenOrderInAnyOtherStatus_whenIsCanceled_shouldReturnFalse() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.cancel());

        Assertions.assertWith(order.status()).isEqualTo(OrderStatus.CANCELED);
        Assertions.assertThat(order.isCanceled()).isTrue();
    }
}
