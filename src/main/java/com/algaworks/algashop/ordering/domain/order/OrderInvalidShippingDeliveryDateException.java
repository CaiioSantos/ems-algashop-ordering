package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainException;

import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST;

public class OrderInvalidShippingDeliveryDateException extends DomainException {
    public OrderInvalidShippingDeliveryDateException(String message) {
        super(message);
    }

    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST,id));
    }
}
