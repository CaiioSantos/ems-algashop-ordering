package com.algaworks.algashop.ordering.core.ports.in.shoppingcart;

import java.util.UUID;

public interface ForManagingShoppintCarts {

    UUID create(UUID customerId);

    void addItem(ShoppingCartItemInput input);

    void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId);

    void empty(UUID shoppingCartId);

    void delete(UUID shoppingCartId);
}
