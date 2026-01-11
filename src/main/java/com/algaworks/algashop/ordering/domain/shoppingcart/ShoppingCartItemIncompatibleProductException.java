package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;
import com.algaworks.algashop.ordering.domain.product.ProductId;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
