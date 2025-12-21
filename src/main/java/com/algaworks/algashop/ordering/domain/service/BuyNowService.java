package com.algaworks.algashop.ordering.domain.service;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.utility.DomainService;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;

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
