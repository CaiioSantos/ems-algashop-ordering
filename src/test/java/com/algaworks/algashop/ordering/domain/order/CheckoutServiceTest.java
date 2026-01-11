package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CheckoutServiceTest {

    private final CheckoutService checkoutService = new CheckoutService();

    @Test
    void givenValidShoppingCart_whenCheckout_shouldReturnPlacedOrderAndEmptyShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.startShopping(ShoppingCartTestDataBuilder.aShoppingCart().customerId);
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItens();
        int expectedOrderItensCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(shoppingCart,billing,shipping,paymentMethod);

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.isPlaced()).isTrue();

        Money expectedTotalAmountWithShipping = shoppingCartTotalAmount.add(shipping.cost());

        Assertions.assertThat(order.totalAmount()).isEqualTo(expectedTotalAmountWithShipping);
        Assertions.assertThat(order.totalItens()).isEqualTo(expectedOrderTotalItems);
        Assertions.assertThat(order.items()).hasSize(expectedOrderItensCount);

        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        Assertions.assertThat(shoppingCart.totalItens()).isEqualTo(Quantity.ZERO);
    }
}
