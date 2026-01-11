package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.customer.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ShoppingServiceTest {

    @InjectMocks
    private ShoppingService shoppingService;

    @Mock
    private ShoppingCarts shoppingCarts;

    @Mock
    private Customers customers;

    @Test
    void givenExistingCustomerAndNoShoppingCart_whenStartShopping_shouldReturnNewShoppingCart() {
        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Mockito.when(customers.exists(customerId)).thenReturn(true);
        Mockito.when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.empty());

        ShoppingCart newShoppingCart = shoppingService.startShopping(customerId);

        Assertions.assertThat(newShoppingCart).isNotNull();
        Assertions.assertThat(newShoppingCart.customerId()).isEqualTo(customerId);
        Assertions.assertThat(newShoppingCart.isEmpty()).isTrue();
        Assertions.assertThat(newShoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        Assertions.assertThat(newShoppingCart.totalItens()).isEqualTo(Quantity.ZERO);

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts).ofCustomer(customerId);
    }

    @Test
    void givenNonExistingCustomer_whenStartShopping_shouldThrowCustomerNotFoundException() {
        CustomerId customerId = new CustomerId();

        Mockito.when(customers.exists(customerId)).thenReturn(false);

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts, never()).ofCustomer(any());
    }

    @Test
    void givenExistingCustomerAndExistingShoppingCart_whenStartShopping_shouldThrowCustomerAlreadyHaveShoppingCartException() {
        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
        ShoppingCart existingCart = ShoppingCartTestDataBuilder.aShoppingCart().customerId(customerId).build();

        Mockito.when(customers.exists(customerId)).thenReturn(true);
        Mockito.when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.of(existingCart));

        Assertions.assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        Mockito.verify(customers).exists(customerId);
        Mockito.verify(shoppingCarts).ofCustomer(customerId);
    }
}
