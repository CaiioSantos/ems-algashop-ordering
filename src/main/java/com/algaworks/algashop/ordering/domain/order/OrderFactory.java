package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;

import java.util.Objects;

public class OrderFactory {
    private OrderFactory() {
    }

    public static Order filled(
        CustomerId customerId,
        Shipping shipping,
        Billing billing,
        PaymentMethod paymentMethod,
        Product product,
        Quantity productQuantity
    )
    {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(productQuantity);

        Order order = Order.draft(customerId);

        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.addItem(product, productQuantity);
        order.changePaymentMethod(paymentMethod);

        return order;
    }
}
