package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    public void souldGenerateBrandNewOrderItem() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(1);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNew()
                .product(product)
                .quantity(quantity)
                .orderId(orderId)
                .build();

        Assertions.assertWith(orderItem,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.productId()).isEqualTo(product.id()),
                o -> Assertions.assertThat(o.productName()).isEqualTo(product.name()),
                o -> Assertions.assertThat(o.price()).isEqualTo(product.price()),
                o -> Assertions.assertThat(o.quantity()).isEqualTo(quantity),
                o -> Assertions.assertThat(o.orderId()).isEqualTo(orderId)
                );
    }
}