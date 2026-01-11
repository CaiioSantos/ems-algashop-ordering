package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainService;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;

@DomainService
public class BuyNowService {


    public Order buyNow(Product product,
                        CustomerId customerId,
                        Billing billing,
                        Shipping shipping,
                        Quantity quantity,
                        PaymentMethod paymentMethod) {

        product.checkOutOfStock();
        Order order = Order.draft(customerId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product,quantity);
        order.place();
        return order;
    }
}
