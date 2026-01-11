package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.product.ProductId;

public interface ShoppingCartProductAdjustmentService {
    void adjustPrice(ProductId productId, Money updatePrice);
    void changeAvailability(ProductId productId,boolean available);
}
