package com.algaworks.algashop.ordering.domain.product;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
