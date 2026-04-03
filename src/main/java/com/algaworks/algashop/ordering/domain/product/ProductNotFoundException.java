package com.algaworks.algashop.ordering.domain.product;

import com.algaworks.algashop.ordering.domain.DomainEntityNotFoundException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;

public class ProductNotFoundException extends DomainEntityNotFoundException {
    public ProductNotFoundException() {
    }
    public ProductNotFoundException(ProductId productId) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, productId));
    }
}
