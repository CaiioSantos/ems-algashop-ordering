package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainException;

import static com.algaworks.algashop.ordering.domain.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedException extends DomainException {

    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED, id, status, newStatus));
    }
}
