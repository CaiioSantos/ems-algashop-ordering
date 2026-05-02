package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    private CheckoutService checkoutService;
    @Mock
    private Orders orders;

    @BeforeEach
    void setup() {
        CustomerHaveFreeShippingSpecification specification = new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
        checkoutService = new CheckoutService(specification);
    }

    @Test
    void givenValidShoppingCart_whenCheckout_shouldReturnPlacedOrderAndEmptyShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));


        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItens();
        int expectedOrderItensCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(customer,shoppingCart,billing,shipping,paymentMethod, new CreditCardId());

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

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(customer.id()).withItems(false).build();
        Product product = ProductTestDataBuilder.aProduct().build();
        shoppingCart.addItem(product, new Quantity(1));

        Product productUnavailable = ProductTestDataBuilder.aProduct().inStock(false).build();
        shoppingCart.refreshItem(productUnavailable);

        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> checkoutService.checkout(customer, shoppingCart, billingInfo, shippingInfo, paymentMethod, new CreditCardId()));

        Assertions.assertThat(shoppingCart.isEmpty()).isFalse();
        Assertions.assertThat(shoppingCart.items()).hasSize(1);
    }

    @Test
    void givenValidShoppingCartAndCustomerWithFreeShipping_whenCheckout_shouldReturnPlacedOrderWithFreeShipping() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().loyaltyPoints(new LoyaltyPoints(3000)).build();

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));


        Billing billingInfo = OrderTestDataBuilder.aBilling();
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItens();
        int expectedOrderItemsCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(customer, shoppingCart, billingInfo, shippingInfo, paymentMethod, new CreditCardId());

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.id()).isNotNull();
        Assertions.assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        Assertions.assertThat(order.billing()).isEqualTo(billingInfo);
        Assertions.assertThat(order.shipping()).isEqualTo(shippingInfo.toBuilder().cost(Money.ZERO).build());
        Assertions.assertThat(order.isPlaced()).isTrue();

        Assertions.assertThat(order.totalAmount()).isEqualTo(shoppingCartTotalAmount);
        Assertions.assertThat(order.totalItens()).isEqualTo(expectedOrderTotalItems);
        Assertions.assertThat(order.items()).hasSize(expectedOrderItemsCount);

        Assertions.assertThat(shoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        Assertions.assertThat(shoppingCart.totalItens()).isEqualTo(Quantity.ZERO);
    }
}
