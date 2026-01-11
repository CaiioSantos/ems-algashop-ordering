package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainException;

import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS;
import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING_INFO;
import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO;
import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD;


public class OrderCannotBePlacedException extends DomainException {
    private OrderCannotBePlacedException(String message) {
        super(message);
    }

    public static OrderCannotBePlacedException noItens(OrderId id) {
        return new OrderCannotBePlacedException(
                String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS, id)
        );
    }
    public static OrderCannotBePlacedException noShippingInfo(OrderId id) {
        return new OrderCannotBePlacedException(
                String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING_INFO, id)
        );
    }
    public static OrderCannotBePlacedException noBillingInfo(OrderId id) {
        return new OrderCannotBePlacedException(
                String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO, id)
        );
    }
    public static OrderCannotBePlacedException noPaymentMethod(OrderId id) {
        return new OrderCannotBePlacedException(
                String.format(ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD, id)
        );
    }
}
