package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;
import com.algaworks.algashop.ordering.domain.product.ProductId;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
