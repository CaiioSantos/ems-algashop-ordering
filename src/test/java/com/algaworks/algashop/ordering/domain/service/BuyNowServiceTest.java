package com.algaworks.algashop.ordering.domain.service;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuyNowServiceTest {

    private final BuyNowService buyNowService = new BuyNowService();

    @Test
    void givenValidProductAndDetails_whenBuyNow_shouldReturnPlacedOrder() {
        Product product = ProductTestDataBuilder.aProduct().build();
        CustomerId customerId = new CustomerId();
        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        Quantity quantity = new Quantity(3);

        Order order = buyNowService.buyNow(product,customerId,billing,shipping,quantity,paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(customerId);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        Assertions.assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        Assertions.assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity).add(shipping.cost());
        Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        Assertions.assertThat(order.totalItens()).isEqualTo(quantity);
    }
}
