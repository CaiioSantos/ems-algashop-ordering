package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD, new CreditCardId()));
        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE, null));
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
