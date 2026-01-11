package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.DomainService;
import com.algaworks.algashop.ordering.domain.product.Product;

@DomainService
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod){

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()){
            throw new ShoppingCartCantProceedToCheckoutException();
        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);

        for(ShoppingCartItem item : shoppingCart.items()){
            order.addItem(new Product(item.productId(), item.name(),
                    item.price(), item.isAvailable()), item.quantity());
        }
        order.place();
        shoppingCart.empty();

        return order;

    }
}
