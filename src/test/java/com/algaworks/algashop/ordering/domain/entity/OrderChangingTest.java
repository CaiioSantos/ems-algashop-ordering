package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exceptions.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.exceptions.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class OrderChangingTest {

    @Test
    public void givenPlacedOrder_whenEditIsAttempted_shouldThrowOrderCannotBeEditedException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(product,new Quantity(2)));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(OrderTestDataBuilder.aShipping()));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBilling(OrderTestDataBuilder.aBilling()));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeItemQuantity(new OrderItemId(),new Quantity(2)));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE));
    }

    @Test
    public void givenDraftOrder_whenRemoveItem_thenItemIsRemoved(){
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        Assertions.assertWith(order.items().size()).isEqualTo(1);
    }

    @Test
    public void givenPlacedOrder_whenRemoveItem_thenThrowOrderCannotBeEditedException(){
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderItem orderItem = order.items().iterator().next();
        order.place();
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(new OrderItemId()));
    }

    @Test
    public void givenInvalidItemId_WhenRemoveItem_thenThrowOrderDoesNotContainOrderItemException(){
        Order order = OrderTestDataBuilder.anOrder().build();
        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(new OrderItemId()));
    }
}
