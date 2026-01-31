package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.product.ProductId;

import java.time.OffsetDateTime;

public record ShoppingCartItemRemovedEvent(ShoppingCartId shoppingCartId,
                                           CustomerId customerId,
                                           ProductId productId,
                                           OffsetDateTime removedAt) {
}
