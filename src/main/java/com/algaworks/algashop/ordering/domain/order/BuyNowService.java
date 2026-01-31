package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainService;
import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@DomainService
@RequiredArgsConstructor
public class BuyNowService {

    private final Orders orders;

    public Order buyNow(Product product,
                        Customer customer,
                        Billing billing,
                        Shipping shipping,
                        Quantity quantity,
                        PaymentMethod paymentMethod) {

        product.checkOutOfStock();
        Order order = Order.draft(customer.id());
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product,quantity);

        if (this.haveFreeShipping(customer)){
            Shipping shippingFree = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(shippingFree);
        }else {
            order.changeShipping(shipping);
        }

        order.place();
        return order;
    }

    private boolean haveFreeShipping(Customer customer) {
        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(100)) >=0
                && orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= 2
                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(2000)) >= 0;
    }
}
