package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.DomainService;
import com.algaworks.algashop.ordering.domain.product.Product;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

    private final CustomerHaveFreeShippingSpecification haveFreeShippingSpecification;


    public Order checkout( Customer customer,
            ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod,
                           CreditCardId creditCardId) {

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()){
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);

        if (this.havFreeShipping(customer)){
            Shipping shippingFree = shipping.toBuilder().cost(Money.ZERO).build();
            order.changeShipping(shippingFree);
        }else {
            order.changeShipping(shipping);
        }

        order.changePaymentMethod(paymentMethod, creditCardId);

        for(ShoppingCartItem item : shoppingCart.items()){
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }

        order.place();
        shoppingCart.empty();

        return order;

    }

    private boolean havFreeShipping(Customer costumer) {
        return haveFreeShippingSpecification.isSatisfiedBy(costumer);
    }
}
