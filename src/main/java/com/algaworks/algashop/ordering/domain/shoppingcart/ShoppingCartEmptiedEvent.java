package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartEmptiedEvent(ShoppingCartId shoppingCartId,
                                       CustomerId customerId,
                                       OffsetDateTime empitedAt) {
}
